package com.example.hyeongwononboarding.domain.survey.service.impl;

import com.example.hyeongwononboarding.domain.survey.dto.request.SubmitSurveyResponseRequest;
import com.example.hyeongwononboarding.domain.survey.dto.request.SubmitSurveyResponseRequest.AnswerDto;
import com.example.hyeongwononboarding.domain.survey.dto.response.SubmitSurveyResponseResponse;
import com.example.hyeongwononboarding.domain.survey.entity.ResponseAnswer;
import com.example.hyeongwononboarding.domain.survey.entity.SurveyResponse;
import com.example.hyeongwononboarding.domain.survey.repository.ResponseAnswerRepository;
import com.example.hyeongwononboarding.domain.survey.repository.SurveyResponseRepository;
import com.example.hyeongwononboarding.domain.survey.service.SurveyResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 설문 응답 서비스 구현체
 */
@Service
@RequiredArgsConstructor
public class SurveyResponseServiceImpl implements SurveyResponseService {
    private final SurveyResponseRepository surveyResponseRepository;
    private final ResponseAnswerRepository responseAnswerRepository;

    @Override
    @Transactional
    public SubmitSurveyResponseResponse submitSurveyResponse(String surveyId, SubmitSurveyResponseRequest request) {
        // SurveyResponse 생성
        SurveyResponse savedSurveyResponse = surveyResponseRepository.save(
                SurveyResponse.builder()
                        .id(UUID.randomUUID().toString())
                        .surveyId(surveyId)
                        .surveyVersionId(request.getSurveyVersionId())
                        .respondentEmail(request.getRespondentEmail())
                        .build()
        );

        // ResponseAnswer 리스트 생성 및 저장
        List<ResponseAnswer> answers = request.getAnswers().stream()
                .map(answerDto -> ResponseAnswer.builder()
                        .id(UUID.randomUUID().toString())
                        .surveyResponse(savedSurveyResponse)
                        .questionId(answerDto.getQuestionId())
                        .answerText(answerDto.getAnswerText())
                        .selectedOptionIds(answerDto.getSelectedOptionIds() != null ? answerDto.getSelectedOptionIds().toString() : null)
                        .build())
                .collect(Collectors.toList());
        responseAnswerRepository.saveAll(answers);

        return SubmitSurveyResponseResponse.builder()
                .responseId(savedSurveyResponse.getId())
                .message("설문 응답이 정상적으로 저장되었습니다.")
                .build();
    }
}
