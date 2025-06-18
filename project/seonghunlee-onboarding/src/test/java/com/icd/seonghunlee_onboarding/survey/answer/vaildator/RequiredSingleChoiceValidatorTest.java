package com.icd.seonghunlee_onboarding.survey.answer.vaildator;

import com.icd.seonghunlee_onboarding.survey.answer.AnswerValidator;
import com.icd.seonghunlee_onboarding.survey.answer.validator.RequiredAnswerValidator;
import com.icd.seonghunlee_onboarding.survey.answer.validator.SingleChoiceValidator;
import org.junit.jupiter.api.Test;

import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

public class RequiredSingleChoiceValidatorTest {

    private final Set<String> validChoices = Set.of("A", "B", "C");
    private final AnswerValidator<String> validator = new RequiredAnswerValidator<>(new SingleChoiceValidator(validChoices));

    @Test
    void null이면_유효하지_않음() {
        assertFalse(validator.isValid(null));
    }

    @Test
    void 빈_문자열이면_유효하지_않음() {
        assertFalse(validator.isValid("   "));
    }

    @Test
    void 올바른_선택지면_유효함() {
        assertTrue(validator.isValid("B"));
    }
}
