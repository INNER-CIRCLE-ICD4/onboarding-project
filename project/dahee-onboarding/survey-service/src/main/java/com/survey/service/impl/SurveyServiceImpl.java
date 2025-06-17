package com.survey.service.impl;

import com.survey.common.dto.*;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
            throw new IllegalArgumentException("설문 항목 개수는 1개 이상 10개 이하만 허용됩니다.");
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
        // [2-1] 중복 응답 체크
        if (responseRepo.existsBySurveyIdAndUuid(surveyId, request.getUuid())) {
            throw new IllegalStateException("이미 응답하셨습니다.");
        }

        // [2-2] 설문 항목 전체 조회
        List<SurveyItem> items = itemRepo.findBySurveyId(surveyId);
        Map<Long, SurveyItem> itemMap = items.stream()
                .collect(Collectors.toMap(SurveyItem::getId, Function.identity()));

        // [2-3] 필수 문항 응답 누락 체크
        for (SurveyItem item : items) {
            if (item.isRequired()) {
                boolean answered = request.getAnswers().stream()
                        .anyMatch(a -> a.getItemId().equals(item.getId())
                                && a.getAnswer() != null && !a.getAnswer().toString().trim().isEmpty());
                if (!answered) {
                    throw new IllegalArgumentException("필수 문항이 누락되었습니다: " + item.getQuestion());
                }
            }
        }

        // [2-4] 각 응답 항목이 설문 문항과 매칭/유효성 체크
        for (ResponseRequest.Answer answer : request.getAnswers()) {
            SurveyItem item = itemMap.get(answer.getItemId());
            if (item == null) {
                throw new IllegalArgumentException("존재하지 않는 문항(itemId=" + answer.getItemId() + ")에 응답했습니다.");
            }
            QuestionType type = item.getType();
            String value = answer.getAnswer();
            List<String> options = item.getOptions();
            if (type == QuestionType.SINGLE_CHOICE) {
                if (options == null || !options.contains(value)) {
                    throw new IllegalArgumentException("선택형 문항에 허용되지 않은 값입니다: " + value);
                }
            } else if (type == QuestionType.MULTI_CHOICE) {
                // 여러 값은 ","로 구분한다고 가정
                if (options == null) {
                    throw new IllegalArgumentException("문항 설정 오류: 선택지가 존재하지 않습니다.");
                }
                for (String v : value.split(",")) {
                    if (!options.contains(v.trim())) {
                        throw new IllegalArgumentException("선택형 문항에 허용되지 않은 값입니다: " + v);
                    }
                }
            }
        }

        // [2-5] 응답 저장
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
    public List<SurveyAnswerResponseDto> getSurveyResponses(Long surveyId) {
        // 1. 해당 설문에 대한 모든 응답 조회
        List<SurveyResponse> responses = responseRepo.findBySurveyId(surveyId);

        if (responses.isEmpty()) {
            return List.of();
        }

        // 2. 응답 ID 리스트로 각 문항별 답변 한 번에 조회
        List<Long> responseIds = responses.stream()
                .map(SurveyResponse::getId)
                .toList();

        List<SurveyResponseItem> responseItems = responseItemRepo.findByResponseIdIn(responseIds);

        // 3. 응답ID → 답변 리스트 맵핑
        Map<Long, List<SurveyResponseItem>> responseItemMap = responseItems.stream()
                .collect(Collectors.groupingBy(SurveyResponseItem::getResponseId));

        // 4. DTO 변환 (SurveyAnswerResponseDto)
        List<SurveyAnswerResponseDto> result = new ArrayList<>();
        for (SurveyResponse response : responses) {
            List<SurveyResponseItem> items = responseItemMap.getOrDefault(response.getId(), List.of());
            List<SurveyAnswerResponseDto.Answer> answers = items.stream()
                    .map(item -> new SurveyAnswerResponseDto.Answer(
                            item.getSurveyItemId(),
                            item.getQuestionText(),
                            item.getAnswer()
                    ))
                    .toList();

            result.add(
                    SurveyAnswerResponseDto.builder()
                            .responseId(response.getId())
                            .surveyId(response.getSurveyId())
                            .uuid(response.getUuid())
                            .submittedAt(response.getSubmittedAt())
                            .answers(answers)
                            .build()
            );
        }
        return result;
    }

}