package onboarding.survey.domain;

import onboarding.survey.exception.BadRequestException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SurveyTest {

    @Test
    void 설문조사_이름이_null이면_예외() {
        assertThrows(BadRequestException.class, () ->
                new Survey(null, "설명", List.of(new SurveyItem("항목 이름", "항목 설명", InputType.SHORT_TEXT, true, null)))
        );
    }

    @Test
    void 설문_받을_항목이_0개면_예외() {
        assertThrows(BadRequestException.class, () ->
                new Survey("설문조사이름", "설문조사설명", List.of())
        );
    }

    @Test
    void 설문_받을_항목_10개_초과면_예외() {
        List<SurveyItem> overItems = IntStream.range(0, 11)
                .mapToObj(i -> new SurveyItem("항목" + i, "항목설명", InputType.SHORT_TEXT, true, null))
                .toList();

        assertThrows(BadRequestException.class, () ->
                new Survey("설문조사이름", "설문조사설명", overItems)
        );
    }

    @Test
    void 설문_생성_성공() {
        Survey survey = new Survey(
                "설문조사 설명",
                "설문조사 설명",
                List.of(new SurveyItem("항목1", "항목설명", InputType.SHORT_TEXT, true, null))
        );

        assertEquals("설문조사 설명", survey.getTitle());
    }

}