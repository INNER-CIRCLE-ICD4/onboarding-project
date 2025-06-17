package com.onboarding.model.response;

public class ShortTextAnswer extends Answer<String> {
    private ShortTextAnswer(String questionId, String answer) {
        super(questionId, answer);
    }

    public static ShortTextAnswer of(String questionId, String answer) {
        validate(questionId, answer);

        return new ShortTextAnswer(questionId, answer);
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

        /* TODO 길이 제한도 생성자로 받도록 수정 */
    }

    @Override
    public String getValue() {
        return answer();
    }
}
