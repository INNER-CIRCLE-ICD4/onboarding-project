package com.onboarding.model;

public class SingleChoiceAnswer extends Answer<String> {

    private SingleChoiceAnswer(String questionId, String answer) {
        super(questionId, answer);
    }

    public static SingleChoiceAnswer of(String questionId, String answer) {
        validate(questionId, answer);
        return new SingleChoiceAnswer(questionId, answer);
    }

    private static void validate(String questionId, String answer) {
        if(questionId == null || answer == null) {
            throw new IllegalArgumentException(INVALID_INPUT);
        }

        if(questionId.isBlank()) {
            throw new IllegalArgumentException(INVALID_QUESTION_ID);
        }

        if(answer.isBlank()) {
            throw new IllegalArgumentException(INVALID_ANSWER);
        }
    }

    @Override
    public String getValue() {
        return answer();
    }
}
