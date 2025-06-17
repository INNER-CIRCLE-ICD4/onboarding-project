package com.icd.seonghunlee_onboarding.answer.survey.answer.vaildator;

import com.icd.seonghunlee_onboarding.answer.survey.answer.AnswerValidator;

import com.icd.seonghunlee_onboarding.answer.survey.answer.validator.LongTextValidator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LongTextValidatorTest {

    private final AnswerValidator validator = new LongTextValidator();

    @Test
    void 글자수가_20자_미만이면_유효하지_않음() {
        String input1 = "짧은 글자"; // 5글자
        String input2 = "이것은 정확히 열아홉 글자"; // 공백 포함 19자

        assertFalse(validator.isValid(input1));
        assertFalse(validator.isValid(input2));
    }

    @Test
    void 연속된_공백이_3칸_이상이면_유효하지_않음() {
        String input = "이 문장은    연속된 공백이 포함되어 유효하지 않습니다."; // 중간에 공백 4칸
        boolean result = validator.isValid(input);
        assertFalse(result);
    }

    @Test
    void 글자수가_101자_이상이면_유효하지_않음() {
        // 101자의 문자열 생성
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 101; i++) {
            sb.append("a");
        }
        String input = sb.toString();

        boolean result = validator.isValid(input);

        assertFalse(result);
    }

    @Test
    void 허용되지_않은_특수문자가_포함되면_유효하지_않음() {
        String input = "이 문장은 특수문자 @를 포함하고 있어서 유효하지 않습니다.";
        boolean result = validator.isValid(input);
        assertFalse(result);
    }

    @Test
    void 허용된_특수문자_포함되도_유효() { // ! @ . ,
        String input = "최소한의 글자 수 유지 후, 특수문자 허용되는지 확인하기~!";
        boolean result = validator.isValid(input);
        assertTrue(result);
    }


}
