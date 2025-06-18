package com.icd.seonghunlee_onboarding.survey.answer.validator;

import com.icd.seonghunlee_onboarding.survey.answer.AnswerValidator;

import java.util.List;
import java.util.Set;

public class MultiChoiceValidator implements AnswerValidator<List<String>> {

    private final Set<String> validChoices;

    public MultiChoiceValidator(Set<String> validChoices) {
        this.validChoices = validChoices;
    }

    @Override
    public boolean isValid(List<String> input) {

        if (input == null) return true;

        if (validChoices == null || validChoices.isEmpty()) return true;

        for (String choice : input) {
            if (!validChoices.contains(choice)) {
                return false;
            }
        }
        return true;
    }
}
