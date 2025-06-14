package com.INNER_CIRCLE_ICD4.innerCircle.service;

import com.INNER_CIRCLE_ICD4.innerCircle.domain.Survey;
import com.INNER_CIRCLE_ICD4.innerCircle.dto.*;
import com.INNER_CIRCLE_ICD4.innerCircle.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyRepository surveyRepository;

    public List<SurveyResponse> findAll() {
        return surveyRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public SurveyResponse findById(UUID id) {
        Survey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("설문이 존재하지 않습니다."));
        return toDto(survey);
    }

    private SurveyResponse toDto(Survey survey) {
        return new SurveyResponse(
                survey.getId(),
                survey.getTitle(),
                survey.getDescription(),
                survey.getVersion(),
                survey.getQuestions().stream()
                        .map(q -> new QuestionResponse(
                                q.getId(),
                                q.getTitle(),
                                q.getDescription(),
                                q.getType(),
                                q.isRequired(),
                                q.getChoices().stream()
                                        .map(c -> new ChoiceResponse(
                                                c.getId(),
                                                c.getText(),
                                                c.getChoiceIndex()
                                        )).toList()
                        )).toList()
        );
    }
}
