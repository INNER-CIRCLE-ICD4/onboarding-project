package onboarding.survey.factory;

import onboarding.survey.domain.InputType;
import onboarding.survey.domain.Survey;
import onboarding.survey.dto.CreateSurveyRequest;
import onboarding.survey.dto.SurveyItemDto;
import onboarding.survey.exception.BadRequestException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SurveyFactoryTest {

    SurveyFactory factory = new SurveyFactory();

    @Test
    void 정상_조립() {
        var itemDto = new SurveyItemDto("항목1", "항목설명", InputType.SINGLE_CHOICE, true, List.of("A", "B"));
        var request = new CreateSurveyRequest("설문조사이름", "설문조사설명", List.of(itemDto));

        Survey survey = factory.createSurvey(request);

        assertEquals("설문조사이름", survey.getTitle());
        assertEquals(1, survey.getItems().size());
    }

    @Test
    void 선택형인데_선택지_없으면_예외() {
        var itemDto = new SurveyItemDto("항목1", "항목설명", InputType.SINGLE_CHOICE, true, List.of());
        var request = new CreateSurveyRequest("설문조사이름", "설문조사설명", List.of(itemDto));

        assertThrows(BadRequestException.class, () -> factory.createSurvey(request));
    }
}