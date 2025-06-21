package com.onboarding.mapper;

import com.onboarding.entity.QuestionEntity;
import com.onboarding.entity.SurveyEntity;
import com.onboarding.model.survey.Question;
import com.onboarding.model.survey.Questions;
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

    public static Survey from(SurveyEntity entity) {
        List<Question> questions = entity.getQuestions().stream()
                .map(QuestionMapper::from)
                .toList();

        return new Survey(
                entity.getId().toString(),
                entity.getTitle(),
                entity.getDescription(),
                new Questions(questions)
        );
    }
}
