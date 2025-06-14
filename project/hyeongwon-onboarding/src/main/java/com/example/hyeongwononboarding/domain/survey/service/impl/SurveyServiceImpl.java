package com.example.hyeongwononboarding.domain.survey.service.impl;

import com.example.hyeongwononboarding.common.util.UUIDGenerator;
import com.example.hyeongwononboarding.domain.survey.dto.request.CreateQuestionRequest;
import com.example.hyeongwononboarding.domain.survey.dto.request.CreateSurveyRequest;
import com.example.hyeongwononboarding.domain.survey.dto.response.QuestionResponse;
import com.example.hyeongwononboarding.domain.survey.dto.response.QuestionResponse.OptionResponse;
import com.example.hyeongwononboarding.domain.survey.dto.response.SurveyResponse;
import com.example.hyeongwononboarding.domain.survey.entity.*;
import com.example.hyeongwononboarding.domain.survey.repository.*;
import com.example.hyeongwononboarding.domain.survey.service.SurveyService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 설문조사 서비스 구현체 설문 생성 로직을 담당합니다. */
@Service
public class SurveyServiceImpl implements SurveyService {
    private final SurveyRepository surveyRepository;
    private final SurveyVersionRepository surveyVersionRepository;
    private final SurveyQuestionRepository surveyQuestionRepository;
    private final QuestionOptionRepository questionOptionRepository;

    public SurveyServiceImpl(SurveyRepository surveyRepository,
                            SurveyVersionRepository surveyVersionRepository,
                            SurveyQuestionRepository surveyQuestionRepository,
                            QuestionOptionRepository questionOptionRepository) {
        this.surveyRepository = surveyRepository;
        this.surveyVersionRepository = surveyVersionRepository;
        this.surveyQuestionRepository = surveyQuestionRepository;
        this.questionOptionRepository = questionOptionRepository;
    }

    /**
     * 설문조사 생성 비즈니스 로직
     */
    @Override
    @Transactional
    public SurveyResponse createSurvey(CreateSurveyRequest request) {
        // 1. Survey 엔티티 생성 및 저장
        String surveyId = UUIDGenerator.generate();
        Survey survey = Survey.builder()
                .id(surveyId)
                .title(request.getTitle())
                .description(request.getDescription())
                .build();
        // 1-1. Survey 엔티티 저장 및 즉시 플러시
        survey = surveyRepository.saveAndFlush(survey);
        
        // 1-2. 저장된 Survey 엔티티를 다시 조회하여 created_at이 설정된 엔티티 가져오기
        survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new IllegalArgumentException("설문조사를 찾을 수 없습니다."));

        // 2. SurveyVersion 엔티티 생성 및 저장
        String versionId = UUIDGenerator.generate();
        SurveyVersion version = SurveyVersion.builder()
                .id(versionId)
                .surveyId(surveyId)
                .version(1)
                .title(request.getTitle())
                .description(request.getDescription())
                .build();
        surveyVersionRepository.save(version);

        // 3. SurveyQuestion, QuestionOption 엔티티 생성 및 저장
        List<QuestionResponse> questionResponses = new ArrayList<>();
        int questionOrder = 1;
        for (CreateQuestionRequest qReq : request.getQuestions()) {
            String questionId = UUIDGenerator.generate();
            SurveyQuestion question = SurveyQuestion.builder()
                    .id(questionId)
                    .surveyVersionId(versionId)
                    .questionOrder(questionOrder++)
                    .name(qReq.getName())
                    .description(qReq.getDescription())
                    .inputType(qReq.getInputType())
                    .isRequired(qReq.getIsRequired())
                    .build();
            surveyQuestionRepository.save(question);

            // 선택형 옵션 생성
            List<OptionResponse> optionResponses = new ArrayList<>();
            if (qReq.getOptions() != null) {
                int optionOrder = 1;
                for (String opt : qReq.getOptions()) {
                    String optionId = UUIDGenerator.generate();
                    QuestionOption option = QuestionOption.builder()
                            .id(optionId)
                            .questionId(questionId)
                            .optionOrder(optionOrder)
                            .optionText(opt)
                            .build();
                    questionOptionRepository.save(option);
                    optionResponses.add(OptionResponse.builder()
                            .id(optionId)
                            .text(opt)
                            .order(optionOrder)
                            .build());
                    optionOrder++;
                }
            }
            questionResponses.add(QuestionResponse.builder()
                    .id(questionId)
                    .name(qReq.getName())
                    .description(qReq.getDescription())
                    .inputType(qReq.getInputType())
                    .isRequired(qReq.getIsRequired())
                    .order(question.getQuestionOrder())
                    .options(optionResponses)
                    .build());
        }

        // 4. SurveyResponse DTO로 변환 및 반환
        return SurveyResponse.builder()
                .surveyId(surveyId)
                .title(survey.getTitle())
                .description(survey.getDescription())
                .version(1)
                .questions(questionResponses)
                .createdAt(survey.getCreatedAt())
                .build();
    }
}
