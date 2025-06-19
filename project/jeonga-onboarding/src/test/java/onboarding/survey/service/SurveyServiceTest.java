package onboarding.survey.service;

import jakarta.transaction.Transactional;
import onboarding.survey.domain.InputType;
import onboarding.survey.dto.CreateSurveyRequest;
import onboarding.survey.dto.SurveyItemDto;
import onboarding.survey.exception.BadRequestException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class SurveyServiceTest {

    @Autowired
    SurveyService surveyService;

    @Test
    void 정상_생성_테스트() {
        var itemDto = new SurveyItemDto(
                "Q1", "설명", InputType.SINGLE_CHOICE, true, List.of("A", "B")
        );
        var request = new CreateSurveyRequest("테스트", "설명", List.of(itemDto));
        Long id = surveyService.createSurvey(request);
        assertNotNull(id);
    }

    @Test
    void 항목_0개_생성시_예외() {
        var request = new CreateSurveyRequest("테스트", "설명", List.of());
        assertThrows(BadRequestException.class, () -> surveyService.createSurvey(request));
    }

    @Test
    void 선택형_선택지_없으면_예외() {
        var itemDto = new SurveyItemDto(
                "Q1", "설명", InputType.SINGLE_CHOICE, true, List.of()
        );
        var request = new CreateSurveyRequest("테스트", "설명", List.of(itemDto));
        assertThrows(BadRequestException.class, () -> surveyService.createSurvey(request));
    }

    @Test
    void 항목_10개_초과시_예외() {
        var itemDtos = IntStream.range(0, 11)
                .mapToObj(i -> new SurveyItemDto("Q" + i, "desc", InputType.SHORT_TEXT, true, null))
                .toList();
        var request = new CreateSurveyRequest("테스트", "설명", itemDtos);
        assertThrows(BadRequestException.class, () -> surveyService.createSurvey(request));
    }
}