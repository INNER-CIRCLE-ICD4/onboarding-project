package com.survey.service.impl;

import com.survey.common.dto.SurveyRequest;
import com.survey.common.dto.ResponseRequest;
import com.survey.common.dto.SurveyResponseDto;
import com.survey.common.dto.ResponseDto;
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
import java.util.List;

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

    @Override
    @Transactional
    public SurveyResponseDto createSurvey(SurveyRequest request) {
        // 1) Survey 엔티티 빌드 및 저장
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

        // 2) SurveyItem 엔티티 생성 및 저장
        Long sid = saved.getId();
        for (SurveyRequest.Item i : request.getItems()) {
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

        // 3) 결과 DTO 반환
        return SurveyResponseDto.builder()
                .surveyId(saved.getId())
                .seriesCode(request.getSeriesCode())
                .version(saved.getVersion())
                .title(saved.getTitle())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    @Override
    @Transactional
    public ResponseDto submitResponse(Long surveyId, ResponseRequest request) {
        // 중복 응답 체크
        if (responseRepo.existsBySurveyIdAndUuid(surveyId, request.getUuid())) {
            throw new IllegalStateException("이미 응답하셨습니다.");
        }

        // SurveyResponse 저장
        SurveyResponse resp = SurveyResponse.builder()
                .surveyId(surveyId)
                .uuid(request.getUuid())
                .submittedAt(LocalDateTime.now())
                .build();
        SurveyResponse savedResp = responseRepo.save(resp);

        // SurveyResponseItem 저장
        Long rid = savedResp.getId();
        for (ResponseRequest.Answer a : request.getAnswers()) {
            String text = itemRepo.findById(a.getItemId())
                    .map(SurveyItem::getQuestion)
                    .orElse("");
            SurveyResponseItem ri = SurveyResponseItem.builder()
                    .responseId(rid)
                    .surveyItemId(a.getItemId())
                    .questionText(text)
                    .answer(a.getAnswer())
                    .build();
            responseItemRepo.save(ri);
        }

        // 결과 DTO 반환
        return ResponseDto.builder()
                .responseId(savedResp.getId())
                .submittedAt(savedResp.getSubmittedAt())
                .build();
    }

    private Long lookupSeriesId(String code) {
        // TODO: SurveySeries 리포지토리에서 code로 조회
        return 1L;
    }
}