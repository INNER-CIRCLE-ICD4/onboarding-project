package com.onboarding.api.dto.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class ShortTextQuestionRes extends QuestionRes {
    public ShortTextQuestionRes(String questionId, String title, String description, String type, boolean required) {
        super(questionId, title, description, type, required);
    }
}
