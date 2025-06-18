package com.icd.seonghunlee_onboarding.answer.survey.answer;

public interface AnswerValidator<T> {
    boolean isValid(T answer);
}
