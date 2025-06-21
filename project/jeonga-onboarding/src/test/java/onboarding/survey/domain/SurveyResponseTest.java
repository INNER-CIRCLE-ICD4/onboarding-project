package onboarding.survey.domain;

import onboarding.survey.exception.BadRequestException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SurveyResponseTest {

    @Test
    void 정상_생성() {
        SurveyResponse r = new SurveyResponse(1L, 2L, "답변");
        assertEquals(1L, r.getSurveyId());
        assertEquals(2L, r.getSurveyItemId());
        assertEquals("답변", r.getAnswer());
    }

    @Test
    void 설문ID가_null이면_예외() {
        BadRequestException ex = assertThrows(BadRequestException.class,
                () -> new SurveyResponse(null, 2L, "답변"));
        assertEquals("설문 ID는 필수입니다.", ex.getMessage());
    }

    @Test
    void 문항ID가_null이면_예외() {
        BadRequestException ex = assertThrows(BadRequestException.class,
                () -> new SurveyResponse(1L, null, "답변"));
        assertEquals("문항 ID는 필수입니다.", ex.getMessage());
    }

    @Test
    void 답변이_null이면_예외() {
        BadRequestException ex = assertThrows(BadRequestException.class,
                () -> new SurveyResponse(1L, 2L, null));
        assertEquals("답변은 빈 값일 수 없습니다.", ex.getMessage());
    }

    @Test
    void 답변이_빈문자열이면_예외() {
        BadRequestException ex = assertThrows(BadRequestException.class,
                () -> new SurveyResponse(1L, 2L, "  "));
        assertEquals("답변은 빈 값일 수 없습니다.", ex.getMessage());
    }
}