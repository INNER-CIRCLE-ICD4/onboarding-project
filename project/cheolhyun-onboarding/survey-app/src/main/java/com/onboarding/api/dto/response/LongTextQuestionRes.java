package com.onboarding.api.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class LongTextQuestionRes extends QuestionRes {
    public LongTextQuestionRes(String questionId, String title, String description, String type, boolean required) {
        super(questionId, title, description, type, required);
    }
}
