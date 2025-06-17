package com.onboarding.model.response;

import java.util.List;

public class MultipleChoiceAnswer extends Answer<List<String>> {
    private static final String TO_MANY_CHOICE = "선택 개수가 많습니다.";

    private MultipleChoiceAnswer(String questionId, List<String> answers) {
        super(questionId, answers);
    }

    public static MultipleChoiceAnswer of(String questionId, List<String> answers) {
        validate(questionId, answers);
        return new MultipleChoiceAnswer(questionId, answers);
    }

    private static void validate(String questionId, List<String> answers) {
        if(questionId == null || answers == null) {
            throw new IllegalArgumentException(INVALID_INPUT);
        }

        if(questionId.isBlank()) {
            throw new IllegalArgumentException(INVALID_QUESTION_ID);
        }

        if(answers.isEmpty()) {
            throw new IllegalArgumentException(INVALID_ANSWER);
        }

        /* TODO 개수 제한도 생성자로 받도록 수정 */
        if(answers.size() > 10) {
            throw new IllegalArgumentException(TO_MANY_CHOICE);
        }
    }

    @Override
    public List<String> getValue() {
        return answer();
    }
}
