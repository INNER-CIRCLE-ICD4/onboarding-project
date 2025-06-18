package com.innercircle.survey.application.service;


import com.innercircle.survey.common.dto.QuestionDto;
import com.innercircle.survey.common.dto.SurveyCreateDto;
import com.innercircle.survey.domain.entity.Question;
import com.innercircle.survey.domain.entity.QuestionOption;
import com.innercircle.survey.domain.entity.Survey;
import com.innercircle.survey.domain.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SurveyService {
    private final SurveyRepository surveyRepository;

    public UUID createSurvey(SurveyCreateDto request) {
        Survey survey = Survey.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .build();

        for (QuestionDto q : request.getQuestions()) {
            Question question = Question.builder()
                    .title(q.getTitle())
                    .description(q.getDescription())
                    .type(q.getType())
                    .required(q.isRequired())
                    .survey(survey)
                    .build();

            if (q.getOptions() != null) {
                for (String option : q.getOptions()) {
                    QuestionOption qo = new QuestionOption(option, question);
                    question.getOptions().add(qo);
                }
            }

            survey.getQuestions().add(question);
        }

        surveyRepository.save(survey);
        return survey.getId(); // UUID 기준
    }

}
