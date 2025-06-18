package com.icd.seonghunlee_onboarding.survey.answer.vaildator;

import com.icd.seonghunlee_onboarding.survey.answer.AnswerValidator;
import com.icd.seonghunlee_onboarding.survey.answer.validator.LongTextValidator;
import com.icd.seonghunlee_onboarding.survey.answer.validator.RequiredAnswerValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RequiredLongTextValidatorTest {

    private final AnswerValidator<String> validator = new RequiredAnswerValidator<>(new LongTextValidator());

    @Test
    void null이면_유효하지_않음() {
        assertFalse(validator.isValid(null));
    }

    @Test
    void 빈_문자열이면_유효하지_않음() {
        assertFalse(validator.isValid("     "));
    }

    @Test
    void 정상적인_장문이면_유효함() {
        String input = "이것은 테스트를 위한 충분히 긴 문장입니다. 허용된 특수문자도 포함합니다!";
        assertTrue(validator.isValid(input));
    }


}
