package com.onboarding.mapper;

import com.onboarding.entity.QuestionEntity;
import com.onboarding.entity.SurveyEntity;
import com.onboarding.model.survey.Survey;

import java.util.List;
import java.util.stream.Collectors;

public class SurveyMapper {
    public static SurveyEntity toEntity(Survey survey) {
        SurveyEntity surveyEntity = new SurveyEntity(survey.getTitle(), survey.getDescription());

        List<QuestionEntity> questionList = survey.getQuestions().getQuestions().stream()
                .map(question -> QuestionMapper.toEntity(surveyEntity, question))
                .collect(Collectors.toList());

        surveyEntity.setQuestions(questionList);
        return surveyEntity;
    }

    public static Survey toDomain(SurveyEntity entity) {
        return new Survey(entity.getTitle(), entity.getDescription());
    }
}
