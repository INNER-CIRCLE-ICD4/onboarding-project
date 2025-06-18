package com.icd.seonghunlee_onboarding.answer.survey.answer.validator;

import com.icd.seonghunlee_onboarding.answer.survey.answer.AnswerValidator;

import java.util.Set;

public class SingleChoiceValidator implements AnswerValidator {

    private final Set<String> validChoices;

    public SingleChoiceValidator(Set<String> validChoices) {
        this.validChoices = validChoices;
    }

    @Override
    public boolean isValid(String input) {

        if (input == null) return true;

        return validChoices.contains(input);
    }

}


