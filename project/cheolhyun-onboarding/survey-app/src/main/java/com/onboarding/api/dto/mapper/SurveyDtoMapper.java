package com.onboarding.api.dto.mapper;

import com.onboarding.api.dto.response.*;
import com.onboarding.model.survey.Question;

import java.util.List;
import java.util.stream.Collectors;

public class SurveyDtoMapper {

    public static QuestionRes from(Question question) {
        return switch (question.getType()) {
            case SHORT_TEXT -> {
                yield new ShortTextQuestionRes(question.getId(), question.getTitle(), question.getDescription(), question.getType().name(), question.isRequired());
            }
            case LONG_TEXT -> {
                yield new LongTextQuestionRes(question.getId(), question.getTitle(), question.getDescription(), question.getType().name(), question.isRequired());
            }
            case SINGLE_CHOICE, MULTI_CHOICE -> {
                List<OptionsRes> optionsResList = question.getOptionList().stream()
                        .map(OptionsRes::of)
                        .collect(Collectors.toList());
                yield new SingleChoiceQuestionRes(question.getId(), question.getTitle(), question.getDescription(), question.getType().name(), question.isRequired(), optionsResList);
            }
            default -> throw new IllegalArgumentException("Unsupported type");
        };
    }
}
