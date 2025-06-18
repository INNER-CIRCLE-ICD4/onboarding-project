package com.onboarding.model.response;

import java.util.List;

public class OptionAnswers {
    private static final String NOT_NULL_ANSWER = "답변 목록을 확인해주세요.";

    private List<OptionAnswer> answers;

    public OptionAnswers(List<OptionAnswer> answers) {
        validate(answers);
        this.answers = answers;
    }

    private void validate(List<OptionAnswer> answers) {
        if(answers == null) {
            throw new IllegalArgumentException(NOT_NULL_ANSWER);
        }
    }
}
