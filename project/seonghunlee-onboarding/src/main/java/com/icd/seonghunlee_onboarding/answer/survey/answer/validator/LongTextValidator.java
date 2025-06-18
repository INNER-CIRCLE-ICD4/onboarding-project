package com.icd.seonghunlee_onboarding.answer.survey.answer.validator;

import com.icd.seonghunlee_onboarding.answer.survey.answer.AnswerValidator;

public class LongTextValidator implements AnswerValidator<String> {

    private static final int MIN_LENGTH = 20;
    private static final int MAX_LENGTH = 100;
    private static final int MAX_CONSECUTIVE_SPACES = 3;
    private static final String ALLOWED_PATTERN = "[a-zA-Z0-9가-힣 \\!~\\.,]*";

    @Override
    public boolean isValid(String input) {

        return hasValidLength(input)
                && hasNoExcessiveSpaces(input)
                && hasOnlyAllowedChars(input);

    }

    private boolean hasValidLength(String input) {
        int length = input.length();
        return length >= MIN_LENGTH && length <= MAX_LENGTH;
    }

    private boolean hasNoExcessiveSpaces(String input) {
        return !input.contains(" ".repeat(MAX_CONSECUTIVE_SPACES));
    }

    private boolean hasOnlyAllowedChars(String input) {
        return input.matches(ALLOWED_PATTERN);
    }
}
