package com.onboarding.model.response;

public class OptionAnswer {
    private String optionId;
    private Answer<?> answer;

    public OptionAnswer(String optionId, Answer<?> answer) {
        this.optionId = optionId;
        this.answer = answer;
    }
}
