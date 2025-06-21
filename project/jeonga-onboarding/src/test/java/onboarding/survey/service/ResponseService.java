package onboarding.survey.service;

import onboarding.survey.domain.InputType;
import onboarding.survey.domain.Survey;
import onboarding.survey.domain.SurveyResponse;
import onboarding.survey.dto.CreateSurveyRequest;
import onboarding.survey.dto.SurveyItemDto;
import onboarding.survey.exception.ApiException;
import onboarding.survey.exception.BadRequestException;
import onboarding.survey.exception.ErrorCode;
import onboarding.survey.repository.SurveyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ResponseServiceTest {

    @Autowired
    private SurveyService surveyService;

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private ResponseService responseService;

    @Test
    void 정상_응답_저장() {
        SurveyItemDto itemDto = new SurveyItemDto(
                "질문1", "설명1", InputType.SHORT_TEXT, true, null
        );
        CreateSurveyRequest createReq = new CreateSurveyRequest(
                "테스트설문", "테스트설명", List.of(itemDto)
        );
        Long surveyId = surveyService.createSurvey(createReq);

        Survey survey = surveyRepository.findById(surveyId).orElseThrow();
        Long itemId = survey.getItems().get(0).getId();

        SurveyResponse saved = responseService.submit(surveyId, itemId, "답변");
        assertNotNull(saved.getId());
    }

    @Test
    void 빈_답변_입력시_예외() {
        SurveyItemDto itemDto = new SurveyItemDto(
                "질문1", "설명1", InputType.SHORT_TEXT, true, null
        );
        Long surveyId = surveyService.createSurvey(
                new CreateSurveyRequest("테스트", "설명", List.of(itemDto))
        );
        Long itemId = surveyRepository.findById(surveyId)
                .orElseThrow().getItems().get(0).getId();

        assertThrows(BadRequestException.class,
                () -> responseService.submit(surveyId, itemId, ""));
    }

    @Test
    void 존재하지_않는_설문ID_요청시_예외() {
        ApiException ex = assertThrows(ApiException.class,
                () -> responseService.submit(9999L, 1L, "답변"));
        assertEquals(ErrorCode.RESOURCE_NOT_FOUND, ex.getErrorCode());
    }

    @Test
    void 존재하지_않는_문항ID_요청시_예외() {
        SurveyItemDto itemDto = new SurveyItemDto(
                "질문1", "설명1", InputType.SHORT_TEXT, true, null
        );
        Long surveyId = surveyService.createSurvey(
                new CreateSurveyRequest("테스트", "설명", List.of(itemDto))
        );

        assertThrows(ApiException.class,
                () -> responseService.submit(surveyId, 9999L, "답변"));
    }
}