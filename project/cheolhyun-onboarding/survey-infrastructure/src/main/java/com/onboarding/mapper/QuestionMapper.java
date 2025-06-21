package com.onboarding.mapper;

import com.onboarding.entity.QuestionEntity;
import com.onboarding.entity.SurveyEntity;
import com.onboarding.model.survey.Question;

public class QuestionMapper {
    public static QuestionEntity toEntity(SurveyEntity surveyEntity, Question question) {
        return new QuestionEntity(question.getTitle(), question.getDescription(), question.getType(), question.isRequired(), surveyEntity);
    }
}
