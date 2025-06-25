package com.survey.service.impl;

import com.survey.common.dto.*;
import com.survey.common.exception.ApplicationException;
import com.survey.common.exception.ErrorCode;
import com.survey.core.entity.Survey;
import com.survey.core.entity.SurveyItem;
import com.survey.core.entity.SurveyResponse;
import com.survey.core.entity.SurveyResponseItem;
import com.survey.core.enums.QuestionType;
import com.survey.core.repository.SurveyItemRepository;
import com.survey.core.repository.SurveyRepository;
import com.survey.core.repository.SurveyResponseItemRepository;
import com.survey.core.repository.SurveyResponseRepository;
import com.survey.service.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * SurveyService 인터페이스의 실제 비즈니스 로직 구현
 * - 트랜잭션 처리, Repository 호출
 * - interface 변경 없이 구현 교체 가능
 */

@Service
@RequiredArgsConstructor
public class SurveyServiceImpl implements SurveyService {
    private final SurveyRepository surveyRepo;
    private final SurveyItemRepository itemRepo;
    private final SurveyResponseRepository responseRepo;
    private final SurveyResponseItemRepository responseItemRepo;

    // 1. 설문 생성
    @Override
    @Transactional
    public SurveyResponseDto createSurvey(SurveyRequest request) {
        // [1-1] 항목 개수 검증
        List<SurveyRequest.Item> items = request.getItems();
        if (items == null || items.size() < 1 || items.size() > 10) {
            throw new ApplicationException(ErrorCode.INVALID_ITEM_COUNT);
        }

        // [1-2] 각 항목 유형별로 보기 개수 제한
        for (SurveyRequest.Item i : items) {
            QuestionType type = QuestionType.valueOf(i.getType());
            List<String> options = i.getOptions();
            if (type == QuestionType.SINGLE_CHOICE || type == QuestionType.MULTI_CHOICE) {
                if (options == null || options.size() < 1 || options.size() > 5) {
                    throw new IllegalArgumentException("선택형 항목의 보기(options)는 1~5개만 허용됩니다. (" + i.getQuestion() + ")");
                }
            }
        }

        // 1. 설문 응답(설문 단위) 엔티티 생성 및 저장
        Long seriesId = lookupSeriesId(request.getSeriesCode());
        Survey survey = Survey.builder()
                .seriesId(seriesId)
                .version(1)
                .title(request.getTitle())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .isOpen(request.getIsOpen())
                .build();
        Survey saved = surveyRepo.save(survey);

        // 2. 각 문항별 응답 항목 저장
        Long sid = saved.getId();
        for (SurveyRequest.Item i : items) {
            SurveyItem item = SurveyItem.builder()
                    .surveyId(sid)
                    .question(i.getQuestion())
                    .description(i.getDescription())
                    .type(QuestionType.valueOf(i.getType()))
                    .required(i.getRequired())
                    .options(i.getOptions())
                    .build();
            itemRepo.save(item);
        }

        // 3. 클라이언트에 응답 DTO 반환
        return SurveyResponseDto.builder()
                .surveyId(saved.getId())
                .seriesCode(request.getSeriesCode())
                .version(saved.getVersion())
                .title(saved.getTitle())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    // 2. 설문 응답 제출
    @Override
    @Transactional
    public ResponseDto submitResponse(Long surveyId, ResponseRequest request) {
        // [1] 중복 응답 체크
        if (responseRepo.existsBySurveyIdAndUuid(surveyId, request.getUuid())) {
            throw new ApplicationException(ErrorCode.DUPLICATE_RESPONSE);
        }

        // [2] 설문 항목 전체 조회 (isDeleted false만 체크 필요시 추가)
        List<SurveyItem> items = itemRepo.findBySurveyIdAndIsDeletedFalse(surveyId);
        if (items == null || items.isEmpty()) {
            throw new ApplicationException(ErrorCode.NOT_FOUND, "응답 대상 설문 문항이 존재하지 않습니다.");
        }
        Map<Long, SurveyItem> itemMap = items.stream()
                .collect(Collectors.toMap(SurveyItem::getId, Function.identity()));

        // [3] 필수 문항 응답 누락 체크
        for (SurveyItem item : items) {
            if (item.isRequired()) {
                boolean answered = request.getAnswers().stream()
                        .anyMatch(a -> a.getItemId().equals(item.getId())
                                && a.getAnswer() != null && !a.getAnswer().toString().trim().isEmpty());
                if (!answered) {
                    throw new ApplicationException(
                            ErrorCode.VALIDATION_FAIL,
                            "필수 문항이 누락되었습니다: " + item.getQuestion()
                    );
                }
            }
        }

        // [4] 각 응답 항목이 설문 문항과 매칭/유효성 체크
        for (ResponseRequest.Answer answer : request.getAnswers()) {
            SurveyItem item = itemMap.get(answer.getItemId());
            if (item == null) {
                throw new ApplicationException(ErrorCode.INVALID_ITEM_ID, answer.getItemId());
            }
            QuestionType type = item.getType();
            String value = answer.getAnswer();
            List<String> options = item.getOptions();

            if (type == QuestionType.SINGLE_CHOICE) {
                if (options == null || !options.contains(value)) {
                    throw new ApplicationException(
                            ErrorCode.VALIDATION_FAIL,
                            "선택형 문항에 허용되지 않은 값입니다: " + value
                    );
                }
            } else if (type == QuestionType.MULTI_CHOICE) {
                if (options == null) {
                    throw new ApplicationException(
                            ErrorCode.VALIDATION_FAIL,
                            "문항 설정 오류: 선택지가 존재하지 않습니다."
                    );
                }
                for (String v : value.split(",")) {
                    if (!options.contains(v.trim())) {
                        throw new ApplicationException(
                                ErrorCode.VALIDATION_FAIL,
                                "선택형 문항에 허용되지 않은 값입니다: " + v
                        );
                    }
                }
            }
        }

        // [5] 응답 저장
        SurveyResponse resp = SurveyResponse.builder()
                .surveyId(surveyId)
                .uuid(request.getUuid())
                .submittedAt(LocalDateTime.now())
                .build();
        SurveyResponse savedResp = responseRepo.save(resp);

        Long rid = savedResp.getId();
        for (ResponseRequest.Answer a : request.getAnswers()) {
            String text = itemMap.get(a.getItemId()) != null ? itemMap.get(a.getItemId()).getQuestion() : "";
            SurveyResponseItem ri = SurveyResponseItem.builder()
                    .responseId(rid)
                    .surveyItemId(a.getItemId())
                    .questionText(text)
                    .answer(a.getAnswer())
                    .build();
            responseItemRepo.save(ri);
        }

        return ResponseDto.builder()
                .responseId(savedResp.getId())
                .submittedAt(savedResp.getSubmittedAt())
                .build();
    }


    // 임시 시리즈 조회 (TODO: 실제 구현 필요)
    private Long lookupSeriesId(String code) {
        // TODO: SurveySeries 리포지토리에서 code로 조회
        return 1L;
    }


    @Override
    @Transactional(readOnly = true)
    public SurveyRequest getSurveyItems(Long surveyId, Integer version) {
        Survey survey = (Survey) ((version == null)
                        ? surveyRepo.findTopByIdOrderByVersionDesc(surveyId).orElseThrow(() -> new ApplicationException(ErrorCode.SURVEY_NOT_FOUND, surveyId)) //최신 버전 조회
                        : surveyRepo.findByIdAndVersion(surveyId, version).orElseThrow(() -> new ApplicationException(ErrorCode.SURVEY_NOT_FOUND, surveyId)));// 특정 버전 조회

        List<SurveyItem> items = itemRepo.findBySurveyIdAndIsDeletedFalse(survey.getId());
        // SurveyRequest.Item 변환
        List<SurveyRequest.Item> itemDtos = items.stream().map(i -> {
            SurveyRequest.Item dto = new SurveyRequest.Item();
            dto.setId(i.getId());
            dto.setQuestion(i.getQuestion());
            dto.setType(i.getType().name());
            dto.setRequired(i.isRequired());
            dto.setOptions(i.getOptions());
            dto.setDescription(i.getDescription());
            return dto;
        }).toList();

        SurveyRequest dto = new SurveyRequest();
        dto.setSeriesCode(survey.getSeriesId().toString());
        dto.setTitle(survey.getTitle());
        dto.setDescription(survey.getDescription());
        dto.setItems(itemDtos);
        dto.setStartDate(survey.getStartDate());
        dto.setEndDate(survey.getEndDate());
        dto.setIsOpen(survey.getIsOpen());
        return dto;
    }


    @Override
    @Transactional(readOnly = true)
    public Page<SurveyAnswerResponseDto> getSurveyResponses(ResponseSearchCondition cond, Pageable pageable) {
        Long   surveySeriesId   = cond.getSurveyId();
        Integer version         = cond.getVersion();
        Long   itemId           = cond.getItemId();
        boolean includeQuestion = Boolean.TRUE.equals(cond.getIncludeQuestion());

        // [1] 시리즈ID 기준으로 응답 페이징 조회 (버전 구분 없이)
        Page<SurveyResponse> responses = responseRepo.findBySurveyId(surveySeriesId, pageable);
        if (responses.isEmpty()) {
            throw new ApplicationException(
                    ErrorCode.NOT_FOUND,
                    "해당 설문 응답이 존재하지 않습니다."
            );
        }

        // [2] 응답에 달린 Survey 버전 정보 조회
        Set<Long> surveyIds = responses.stream()
                .map(SurveyResponse::getSurveyId)
                .collect(Collectors.toSet());
        Map<Long, Integer> surveyVersionMap = surveyRepo.findAllById(surveyIds).stream()
                .collect(Collectors.toMap(Survey::getId, Survey::getVersion));

        // [3] 버전 필터링 (메모리)
        if (version != null) {
            List<SurveyResponse> filtered = responses.getContent().stream()
                    .filter(resp -> surveyVersionMap.getOrDefault(resp.getSurveyId(), -1).equals(version))
                    .toList();

            if (filtered.isEmpty()) {
                throw new ApplicationException(
                        ErrorCode.NOT_FOUND,
                        "버전 " + version + " 에 해당하는 응답이 존재하지 않습니다."
                );
            }
            responses = new PageImpl<>(filtered, pageable, filtered.size());
        }

        // [4] 응답 아이템 전체 조회
        List<Long> responseIds = responses.stream()
                .map(SurveyResponse::getId)
                .toList();
        List<SurveyResponseItem> responseItems = responseItemRepo.findByResponseIdIn(responseIds);
        if (responseItems.isEmpty()) {
            throw new ApplicationException(
                    ErrorCode.NOT_FOUND,
                    "응답 항목 데이터가 없습니다."
            );
        }

        // [5] itemId 필터링
        if (itemId != null) {
            responseItems = responseItems.stream()
                    .filter(item -> item.getSurveyItemId().equals(itemId))
                    .toList();
            if (responseItems.isEmpty()) {
                throw new ApplicationException(
                        ErrorCode.NOT_FOUND,
                        "문항 ID " + itemId + " 에 해당하는 응답이 없습니다."
                );
            }
        }

        // [6] responseId별 그룹핑
        Map<Long, List<SurveyResponseItem>> responseItemMap =
                responseItems.stream().collect(Collectors.groupingBy(SurveyResponseItem::getResponseId));

        // [7] DTO 변환 (includeQuestion에 따라 questionText 포함/제외)
        List<SurveyAnswerResponseDto> dtos = responses.stream().map(resp -> {
            List<SurveyResponseItem> items = responseItemMap.getOrDefault(resp.getId(), List.of());
            List<SurveyAnswerResponseDto.Answer> answers = items.stream()
                    .map(item -> {
                        String questionText = includeQuestion
                                ? item.getQuestionText()
                                : null;
                        return new SurveyAnswerResponseDto.Answer(
                                item.getSurveyItemId(),
                                questionText,
                                item.getAnswer()
                        );
                    })
                    .toList();

            // 혹시 키가 없다면 디폴트(예: 1) 를 넣도록 getOrDefault 로 처리합니다.
            Integer respVersion = surveyVersionMap.getOrDefault(
                    resp.getSurveyId(),  // map 의 key: Survey.id
                    1                     // map 에 없을 때 기본 버전
            );
            return SurveyAnswerResponseDto.builder()
                    .responseId(resp.getId())
                    .surveyId(resp.getSurveyId())
                    .version(respVersion)
                    .uuid(resp.getUuid())
                    .submittedAt(resp.getSubmittedAt())
                    .answers(answers)
                    .build();
        }).toList();

        if (dtos.isEmpty()) {
            throw new ApplicationException(
                    ErrorCode.NOT_FOUND,
                    "응답 결과 데이터가 없습니다."
            );
        }

        // [8] 페이징 정보 유지하면서 DTO 리스트 반환
        return new PageImpl<>(dtos, pageable, responses.getTotalElements());
    }



    //수정하는 경우 과거 버전은 ispoen -> 취소 ,신규버전 isopen ,
    //고객은??
    @Override
    @Transactional
    public SurveyResponseDto updateSurvey(Long surveyId, SurveyRequest request) {
        // 1. 기존 설문, 기존 문항 목록 조회
        Survey oldSurvey = surveyRepo.findById(surveyId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND));
        List<SurveyItem> oldItems = itemRepo.findBySurveyId(surveyId);

        // 2. 요청에서 온 최종 문항 목록
        List<SurveyRequest.Item> newItemsReq = request.getItems();

        // 3. 구조가 변경됐는지 비교(문항 개수/내용이 다르면 새 버전)
        boolean structureChanged = isStructureChanged(oldItems, newItemsReq);

        Survey newSurvey;
        if (structureChanged) {
            // 4-1. 새 버전 설문 생성 (Deep Copy)
            newSurvey = Survey.builder()
                    .seriesId(oldSurvey.getSeriesId())
                    .version(oldSurvey.getVersion() + 1)
                    .title(request.getTitle())
                    .description(request.getDescription())
                    .startDate(request.getStartDate())
                    .endDate(request.getEndDate())
                    .isOpen(request.getIsOpen())
                    .createdAt(LocalDateTime.now())
                    .build();
            surveyRepo.save(newSurvey);

            // 새 설문에 대해 문항 전체 재등록
            List<SurveyItem> itemsToSave = newItemsReq.stream()
                    .map(req -> SurveyItem.builder()
                            .surveyId(newSurvey.getId())
                            .question(req.getQuestion())
                            .type(QuestionType.valueOf(req.getType()))
                            .required(req.getRequired())
                            .options(req.getOptions())
                            .description(req.getDescription())
                            .isDeleted(false)
                            .build())
                    .toList();
            itemRepo.saveAll(itemsToSave);
        } else {
            // 4-2. 구조 변경 없으면 기존 설문만 update
            oldSurvey.setTitle(request.getTitle());
            oldSurvey.setDescription(request.getDescription());
            oldSurvey.setStartDate(request.getStartDate());
            oldSurvey.setEndDate(request.getEndDate());
            oldSurvey.setIsOpen(request.getIsOpen());
            surveyRepo.save(oldSurvey);

            // 기존 문항 수정/삭제/추가 반영
            updateSurveyItems(oldItems, newItemsReq, oldSurvey.getId());
            newSurvey = oldSurvey;
        }

        // 5. 반환 DTO 변환
        return SurveyResponseDto.builder()
                .surveyId(newSurvey.getId())
                .seriesCode("") // 필요 시 채우기
                .version(newSurvey.getVersion())
                .title(newSurvey.getTitle())
                .createdAt(newSurvey.getCreatedAt())
                .build();
    }


    private boolean isStructureChanged(List<SurveyItem> oldItems, List<SurveyRequest.Item> newItems) {
        if (oldItems.size() != newItems.size()) return true;
        for (int i = 0; i < oldItems.size(); i++) {
            SurveyItem old = oldItems.get(i);
            SurveyRequest.Item ni = newItems.get(i);
            // 문항 내용, 타입, 옵션 등 구조적으로 바뀐 경우만 true
            if (!old.getQuestion().equals(ni.getQuestion()) ||
                    !old.getType().name().equals(ni.getType()) ||
                    !old.getOptions().equals(ni.getOptions())) {
                return true;
            }
        }
        return false;
    }

    private void updateSurveyItems(List<SurveyItem> oldItems, List<SurveyRequest.Item> newItemsReq, Long surveyId) {
        // 기존 id 집합
        Set<Long> oldIds = oldItems.stream().map(SurveyItem::getId).collect(Collectors.toSet());
        // 요청 id 집합(없으면 신규)
        Set<Long> newIds = newItemsReq.stream().map(SurveyRequest.Item::getId).filter(Objects::nonNull).collect(Collectors.toSet());

        // 삭제: 기존엔 있지만 요청에 없는 id
        oldIds.stream().filter(id -> !newIds.contains(id)).forEach(id -> itemRepo.deleteById(id));

        // 수정/추가
        for (SurveyRequest.Item ni : newItemsReq) {
            if (ni.getId() != null && oldIds.contains(ni.getId())) {
                // 수정
                SurveyItem item = itemRepo.findById(ni.getId()).orElseThrow();
                item.setQuestion(ni.getQuestion());
                item.setType(QuestionType.valueOf(ni.getType()));
                item.setRequired(ni.getRequired());
                item.setOptions(ni.getOptions());
                item.setDescription(ni.getDescription());
                itemRepo.save(item);
            } else {
                // 추가
                SurveyItem newItem = SurveyItem.builder()
                        .surveyId(surveyId)
                        .question(ni.getQuestion())
                        .type(QuestionType.valueOf(ni.getType()))
                        .required(ni.getRequired())
                        .options(ni.getOptions())
                        .description(ni.getDescription())
                        .isDeleted(false)
                        .build();
                itemRepo.save(newItem);
            }
        }
    }


}