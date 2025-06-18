package com.icd.seonghunlee_onboarding.answer.survey.answer.vaildator;

import com.icd.seonghunlee_onboarding.answer.survey.answer.AnswerValidator;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class MultiChoiceValidatorTest {

    private final AnswerValidator<List<String>> validator =
            new MultiChoiceValidator(Set.of("옵션1", "옵션2", "옵션3"));

    @Test
    void 허용된_선택지만_포함되었을_때_유효함() {
        List<String> input = List.of("옵션1", "옵션3");
        boolean result = validator.isValid(input);
        assertTrue(result);
    }

    @Test
    void 유효하지_않은_값이_포함되면_유효하지_않음() {
        List<String> input = List.of("옵션1", "망고"); // "망고"는 validChoices에 없음
        boolean result = validator.isValid(input);
        assertFalse(result);
    }

    @Test
    void 선택지가_비어있으면_모든_입력은_유효함() {
        AnswerValidator<List<String>> validator1 =
                new MultiChoiceValidator(Set.of());
        List<String> input = List.of("아무거나", "또는 이것");
        boolean result = validator1.isValid(input);
        assertTrue(result);
    }

    @Test
    void 입력값이_null이면_유효함() {
        List<String> input = null;
        boolean result = validator.isValid(input);
        assertTrue(result);
    }

    @Test
    void 입력값이_빈리스트이면_유효함() {
        List<String> input = List.of();
        boolean result = validator.isValid(input);
        assertTrue(result);
    }




}
