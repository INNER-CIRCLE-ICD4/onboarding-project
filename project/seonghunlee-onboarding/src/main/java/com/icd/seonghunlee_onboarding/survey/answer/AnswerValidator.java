package com.icd.seonghunlee_onboarding.survey.answer;

public interface AnswerValidator<T> {
    boolean isValid(T answer);
}
