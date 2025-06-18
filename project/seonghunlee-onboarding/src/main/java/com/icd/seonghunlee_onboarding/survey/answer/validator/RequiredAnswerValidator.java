package com.icd.seonghunlee_onboarding.survey.answer.validator;

import com.icd.seonghunlee_onboarding.survey.answer.AnswerValidator;

import java.util.Collection;

public class RequiredAnswerValidator<T> implements AnswerValidator<T> {

    private final AnswerValidator<T> delegate;

    public RequiredAnswerValidator(AnswerValidator<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean isValid(T answer) {
        // 1. null은 무조건 false
        if (answer == null) return false;

        // 2. 타입별로 처리
        if (answer instanceof String str) {
            if (str.trim().isEmpty()) return false;
        } else if (answer instanceof Collection<?> collection) {
            if (collection.isEmpty()) return false;
        }

        // 3. 위 조건 통과하면 원래 validator에 위임
        return delegate.isValid(answer);
    }

}
