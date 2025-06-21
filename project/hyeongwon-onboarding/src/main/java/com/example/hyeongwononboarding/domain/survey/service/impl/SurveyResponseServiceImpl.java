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

        // SubmitSurveyResponseResponse의 실제 필드만 세팅합니다.
        // answers는 ResponseAnswer 리스트를 AnswerResponse 리스트로 변환합니다.
        List<SubmitSurveyResponseResponse.AnswerResponse> answerResponses = answers.stream().map(answer ->
                SubmitSurveyResponseResponse.AnswerResponse.builder()
                        .questionId(answer.getQuestionId())
                        .questionName(null) // 필요시 채워주세요
                        .answerText(answer.getAnswerText())
                        .selectedOptions(null) // 필요시 변환 로직 추가
                        .build()
        ).collect(Collectors.toList());

        // surveyVersionId(String)를 Integer로 변환하여 세팅합니다. 변환 실패 시 null 처리
        Integer surveyVersion = null;
        try {
            surveyVersion = savedSurveyResponse.getSurveyVersionId() != null ? Integer.parseInt(savedSurveyResponse.getSurveyVersionId()) : null;
        } catch (NumberFormatException e) {
            // 변환 실패 시 null 반환, 필요시 로깅 또는 예외 처리 추가 가능
        }

        return SubmitSurveyResponseResponse.builder()
                .responseId(savedSurveyResponse.getId())
                .surveyId(savedSurveyResponse.getSurveyId())
                .surveyVersion(surveyVersion)
                .respondentEmail(savedSurveyResponse.getRespondentEmail())
                .submittedAt(savedSurveyResponse.getSubmittedAt())
                .answers(answerResponses)
                .build();
    }
}
