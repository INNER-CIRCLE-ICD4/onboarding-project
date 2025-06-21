package com.onboarding.mapper;

import com.onboarding.entity.OptionEntity;
import com.onboarding.entity.QuestionEntity;
import com.onboarding.entity.SurveyEntity;
import com.onboarding.model.survey.Option;
import com.onboarding.model.survey.Options;
import com.onboarding.model.survey.Question;

import java.util.List;
import java.util.stream.Collectors;

public class QuestionMapper {
    public static QuestionEntity toEntity(SurveyEntity surveyEntity, Question question) {
        QuestionEntity questionEntity = new QuestionEntity(question.getTitle(), question.getDescription(), question.getType(), question.isRequired(), surveyEntity);

        if(question.getOptionList() != null) {
            List<OptionEntity> optionEntities = question.getOptionList().stream()
                    .map(option -> OptionMapper.toEntity(questionEntity, option))
                    .collect(Collectors.toList());

            questionEntity.setOptions(optionEntities);
        }

        return questionEntity;
    }

    public static Question from(QuestionEntity questionEntity) {
        List<Option> optionList = questionEntity.getOptions().stream()
                .map(OptionMapper::from)
                .collect(Collectors.toList());

        Options options = new Options();
        options.setOptionList(optionList);

        return new Question(
                questionEntity.getId().toString(),
                questionEntity.getTitle(),
                questionEntity.getDescription(),
                questionEntity.getType(),
                options,
                questionEntity.isRequired()
        );
    }
}
