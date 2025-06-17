package com.icd.seonghunlee_onboarding.answer.survey.answer;

import com.icd.seonghunlee_onboarding.answer.survey.answer.validator.ShortTextValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ShortTextValidatorTest {

    private final AnswerValidator validator = new ShortTextValidator();

    @Test
    void 공백이_두_개_이상_연속되면_유효하지_않다() {
        ShortTextValidator validator = new ShortTextValidator();

        String input = "안녕  하세요";  // 공백 2칸
        boolean result = validator.isValid(input);

        assertFalse(result);
    }

    @Test
    void 공백이_두_번_있지만_연속되지_않으면_유효하지_않음() { // 띄어쓰기 2번이면, 단답형으로 보지 않음
        ShortTextValidator validator = new ShortTextValidator();
        String input = "공백 두 칸"; // 공백은 총 2번 있으나 연속 아님
        boolean result = validator.isValid(input);
        assertFalse(result);
    }

    @Test
    void 글자_11자는_유효하지_않음() {
        ShortTextValidator validator = new ShortTextValidator();
        String input = "abcdefghijk"; // 11글자
        boolean result = validator.isValid(input);
        assertFalse(result);
    }

    @Test
    void 글자_10자_이하는_유효함() {
        ShortTextValidator validator = new ShortTextValidator();
        String input = "abcdefghij"; // 딱 10글자
        boolean result = validator.isValid(input);
        assertTrue(result);
    }

    @Test
    void 특수문자가_포함되면_유효하지_않음() {
        ShortTextValidator validator = new ShortTextValidator();
        String input = "테스트@문자열!"; // @, ! 특수문자 포함
        boolean result = validator.isValid(input);
        assertFalse(result);
    }

    @Test
    void 입력_양끝_공백이_있어도_검증에_영향_없어야_한다() {
        ShortTextValidator validator = new ShortTextValidator();
        String input = " 공백 하나 ";
        boolean result = validator.isValid(input);
        assertTrue(result);
    }


}
