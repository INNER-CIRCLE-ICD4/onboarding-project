package com.icd.seonghunlee_onboarding.survey.answer.vaildator;

import com.icd.seonghunlee_onboarding.survey.answer.AnswerValidator;
import com.icd.seonghunlee_onboarding.survey.answer.validator.RequiredAnswerValidator;
import com.icd.seonghunlee_onboarding.survey.answer.validator.ShortTextValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RequiredShortTextValidatorTest {

    private final AnswerValidator<String> validator = new RequiredAnswerValidator<>(new ShortTextValidator());

    @Test
    void null이면_유효하지_않음() {
        assertFalse(validator.isValid(null));
    }

    @Test
    void 공백만_있으면_유효하지_않음() {
        assertFalse(validator.isValid("   "));
    }

    @Test
    void 정상값이면_유효함() {
        assertTrue(validator.isValid("정상값"));
    }


}
