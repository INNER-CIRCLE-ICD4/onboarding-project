package com.icd.seonghunlee_onboarding.survey.answer.vaildator;

import com.icd.seonghunlee_onboarding.survey.answer.AnswerValidator;
import com.icd.seonghunlee_onboarding.survey.answer.validator.MultiChoiceValidator;
import com.icd.seonghunlee_onboarding.survey.answer.validator.RequiredAnswerValidator;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

public class RequiredMultiChoiceValidatorTest {

    private final Set<String> validChoices = Set.of("1", "2", "3");
    private final AnswerValidator<List<String>> validator = new RequiredAnswerValidator<>(new MultiChoiceValidator(validChoices));

    @Test
    void null이면_유효하지_않음() {
        assertFalse(validator.isValid(null));
    }

    @Test
    void 빈_리스트면_유효하지_않음() {
        assertFalse(validator.isValid(List.of()));
    }

    @Test
    void 선택지_포함한_리스트는_유효함() {
        assertTrue(validator.isValid(List.of("1", "2")));
    }
}
