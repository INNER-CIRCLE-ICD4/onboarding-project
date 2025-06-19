package onboarding.survey.domain;

import onboarding.survey.exception.BadRequestException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ChoiceTest {

    @Test
    void 선택지_값이_null이면_예외() {
        assertThrows(BadRequestException.class, () -> new Choice(null));
    }

    @Test
    void 선택지_값이_빈문자면_예외() {
        assertThrows(BadRequestException.class, () -> new Choice(""));
    }

    @Test
    void 선택지_값이_공백이면_예외() {
        assertThrows(BadRequestException.class, () -> new Choice("   "));
    }

    @Test
    void 선택지_생성_성공() {
        Choice choice = new Choice("선택지1");
        assertEquals("선택지1", choice.getOptionValue());
    }

}