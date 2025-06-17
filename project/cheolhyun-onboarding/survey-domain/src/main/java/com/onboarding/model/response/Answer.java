package com.onboarding.model.response;

public abstract class Answer<T> implements IAnswerValue<T> {
    protected static final String INVALID_INPUT = "필수 입력 값 중에 없는 값이 있습니다.";
    protected static final String INVALID_QUESTION_ID = "어떤 설문 항목인지 알 수 없습니다.";
    protected static final String INVALID_ANSWER = "답변이 올바르지 않습니다.";

    private String questionId;
    private T answer;

    Answer(String questionId, T answer) {
        this.questionId = questionId;
        this.answer = answer;
    }

    protected String questionId() {
        return questionId;
    }

    protected T answer() {
        return answer;
    }

    @Override
    public abstract T getValue();
}
