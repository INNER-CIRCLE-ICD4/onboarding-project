package com.icd.seonghunlee_onboarding.survey.answer.validator;

import com.icd.seonghunlee_onboarding.survey.answer.AnswerValidator;

import java.util.Set;

public class SingleChoiceValidator implements AnswerValidator<String> {

    private final Set<String> validChoices;

    public SingleChoiceValidator() {
        this.validChoices = null; // 선택지가 없는 경우 값은 무조건 유효.
    }

    public SingleChoiceValidator(Set<String> validChoices) {
        this.validChoices = validChoices;
    }

    @Override
    public boolean isValid(String input) {

        if (input == null) return true;

        if (validChoices == null || validChoices.isEmpty()) {
            return true; // 선택지가 없으면 어떤 값도 허용
        }

        return validChoices.contains(input);
    }

}


