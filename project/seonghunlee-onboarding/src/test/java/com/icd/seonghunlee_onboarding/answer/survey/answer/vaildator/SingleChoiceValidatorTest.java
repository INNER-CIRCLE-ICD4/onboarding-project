package com.icd.seonghunlee_onboarding.answer.survey.answer.vaildator;

import com.icd.seonghunlee_onboarding.answer.survey.answer.AnswerValidator;
import com.icd.seonghunlee_onboarding.answer.survey.answer.validator.SingleChoiceValidator;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class SingleChoiceValidatorTest {

    @Test
    void null은_유효한_값이다() {
        AnswerValidator validator = new SingleChoiceValidator(Set.of("A", "B", "C"));
        String input = null;
        boolean result = validator.isValid(input);
        assertTrue(result);
    }

    @Test
    void 선택지에_있는_값이면_유효함() {
        AnswerValidator validator = new SingleChoiceValidator(Set.of("A", "B", "C"));
        String input = "C";
        assertTrue(validator.isValid(input));
    }

    @Test
    void 선택지에_없는_값이면_유효하지_않음() {
        AnswerValidator validator = new SingleChoiceValidator(Set.of("A", "B", "C"));
        String input = "4번";
        assertFalse(validator.isValid(input));
    }

    @Test
    void 선택지_목록이_없으면_무조건_유효() {
        AnswerValidator validator = new SingleChoiceValidator();
        String input = "anyAnswer";  // 어떤 답변이 와도
        boolean result = validator.isValid(input);
        assertTrue(result);
    }

}
