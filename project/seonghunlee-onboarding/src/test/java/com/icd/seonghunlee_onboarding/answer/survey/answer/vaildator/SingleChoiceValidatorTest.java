package com.icd.seonghunlee_onboarding.answer.survey.answer.vaildator;

import com.icd.seonghunlee_onboarding.answer.survey.answer.AnswerValidator;
import com.icd.seonghunlee_onboarding.answer.survey.answer.validator.SingleChoiceValidator;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class SingleChoiceValidatorTest {

    private final AnswerValidator validator = new SingleChoiceValidator(Set.of("A", "B", "C"));

    @Test
    void null은_유효한_값이다() {
        String input = null;
        boolean result = validator.isValid(input);
        assertTrue(result);
    }

    @Test
    void 선택지에_있는_값이면_유효함() {
        String input = "C";
        assertTrue(validator.isValid(input));
    }

    @Test
    void 선택지에_없는_값이면_유효하지_않음() {
        String input = "4번";
        assertFalse(validator.isValid(input));
    }


}
