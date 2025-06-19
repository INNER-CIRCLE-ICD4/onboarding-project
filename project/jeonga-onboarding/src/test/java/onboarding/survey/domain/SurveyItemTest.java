package onboarding.survey.domain;

import onboarding.survey.exception.BadRequestException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SurveyItemTest {

    @Test
    void name_null이면_예외() {
        assertThrows(BadRequestException.class, () ->
                new SurveyItem(null, "desc", InputType.SHORT_TEXT, true, null)
        );
    }

    @Test
    void 선택형인데_선택지_null이면_예외() {
        assertThrows(BadRequestException.class, () ->
                new SurveyItem("항목1", "desc", InputType.SINGLE_CHOICE, true, null)
        );
    }

    @Test
    void 선택형인데_선택지_빈_목록이면_예외() {
        assertThrows(BadRequestException.class, () ->
                new SurveyItem("항목1", "desc", InputType.SINGLE_CHOICE, true, List.of())
        );
    }

    @Test
    void 선택형이_아니면_선택지_없어도_OK() {
        assertDoesNotThrow(() ->
                new SurveyItem("항목1", "desc", InputType.SHORT_TEXT, true, null)
        );
    }

}
