package com.onboarding.mapper;

import com.onboarding.entity.OptionEntity;
import com.onboarding.entity.QuestionEntity;
import com.onboarding.model.survey.Option;

public class OptionMapper {
    public static OptionEntity toEntity(QuestionEntity questionEntity, Option option) {
        return new OptionEntity(option.getText(), questionEntity);
    }

    public static Option from(OptionEntity optionEntity) {
        return new Option(
                optionEntity.getId().toString(),
                optionEntity.getText()
        );
    }
}
