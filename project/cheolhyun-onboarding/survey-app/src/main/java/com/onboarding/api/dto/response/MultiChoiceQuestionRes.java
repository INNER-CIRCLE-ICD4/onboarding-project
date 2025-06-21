package com.onboarding.api.dto.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@Getter
@EqualsAndHashCode(callSuper = true)
public class MultiChoiceQuestionRes extends QuestionRes {
    private List<OptionsRes> options;

    public MultiChoiceQuestionRes(String questionId, String title, String description, String type, boolean required, List<OptionsRes> options) {
        super(questionId, title, description, type, required);
        this.options = options;
    }
}
