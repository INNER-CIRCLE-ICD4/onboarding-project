package com.innercircle.survey.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.innercircle.survey.common.dto.AnswerDto;
import com.innercircle.survey.common.dto.SurveyResponseDto;
import com.innercircle.survey.domain.entity.Question;
import com.innercircle.survey.domain.entity.Survey;
import com.innercircle.survey.domain.entity.SurveyResponse;
import com.innercircle.survey.domain.repository.SurveyRepository;
import com.innercircle.survey.domain.repository.SurveyResponseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SurveyResponseService {

    private final SurveyRepository surveyRepository;
    private final SurveyResponseRepository surveyResponseRepository;
    private final ObjectMapper objectMapper;

    public void submitResponse(SurveyResponseDto request) {
        // 1. 설문 존재 여부 확인
        Survey survey = surveyRepository.findById(request.getSurveyId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 설문입니다."));

        // 2. 질문 ID 유효성 검증
        Set<UUID> validQuestionIds = survey.getQuestions().stream()
                .map(Question::getId)
                .collect(Collectors.toSet());

        for (AnswerDto answer : request.getAnswers()) {
            UUID qid = UUID.fromString(answer.getQuestionId());
            if (!validQuestionIds.contains(qid)) {
                throw new IllegalArgumentException("유효하지 않은 질문 ID입니다: " + qid);
            }
        }

        // 3. 응답 및 질문 스냅샷 JSON 직렬화
        String answersJson = toJson(request.getAnswers());
        String snapshotJson = toJson(survey.getQuestions());

        // 4. 저장
        SurveyResponse response = new SurveyResponse(
                survey.getId(),
                answersJson,
                snapshotJson
        );
        surveyResponseRepository.save(response);
    }

    private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 변환 실패", e);
        }
    }
}