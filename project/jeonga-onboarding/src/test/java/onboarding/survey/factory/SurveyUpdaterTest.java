package onboarding.survey.factory;

import onboarding.survey.domain.Survey;
import onboarding.survey.domain.SurveyItem;
import onboarding.survey.dto.CreateSurveyRequest;
import onboarding.survey.dto.SurveyItemDto;
import onboarding.survey.dto.UpdateSurveyItemDto;
import onboarding.survey.dto.UpdateSurveyRequest;
import onboarding.survey.domain.InputType;
import onboarding.survey.exception.BadRequestException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SurveyUpdaterTest {

    private final SurveyFactory factory = new SurveyFactory();
    private final SurveyUpdater updater = new SurveyUpdater();

    @Test
    void 정상_업데이트() {
        // 기존 설문 생성
        var itemDto = new SurveyItemDto(
                "질문1", "설명1",
                InputType.SINGLE_CHOICE, true,
                List.of("선택A", "선택B")
        );

        Survey survey = factory.createSurvey(
                new CreateSurveyRequest("설문제목", "설문설명", List.of(itemDto))
        );

        // 기존 항목 수정
        var existingDto = new UpdateSurveyItemDto(
                survey.getItems().get(0).getId(),
                "질문1-수정", "설명1-수정",
                InputType.SINGLE_CHOICE, false,
                List.of("선택X")
        );

        // 새 항목 추가
        var newDto = new UpdateSurveyItemDto(
                null, "질문2", "설명2",
                InputType.SHORT_TEXT, true,
                null
        );
        var request = new UpdateSurveyRequest(
                "설문제목-업데이트",
                "설문설명-업데이트",
                List.of(existingDto, newDto)
        );

        updater.applyUpdates(survey, request);

        // 검증
        assertEquals("설문제목-업데이트", survey.getTitle());
        assertEquals("설문설명-업데이트", survey.getDescription());
        assertEquals(2, survey.getItems().size());

        // 수정된 기존 항목 확인
        SurveyItem updated = survey.getItems().stream()
                .filter(i -> "질문1-수정".equals(i.getName()))
                .findFirst().orElseThrow();

        assertEquals("질문1-수정", updated.getName());
        assertFalse(updated.isRequired());
        assertEquals("선택X", updated.getChoices().get(0).getOptionValue());

        // 추가된 새 항목 확인
        SurveyItem added = survey.getItems().stream()
                .filter(i -> "질문2".equals(i.getName()))
                .findFirst().orElseThrow();
        assertEquals("질문2", added.getName());
    }

    @Test
    void 존재하지_않는_항목_id_예외() {
        // 기존 설문 생성
        var itemDto = new SurveyItemDto(
                "질문1", "설명1",
                InputType.SHORT_TEXT, true,
                null
        );
        Survey survey = factory.createSurvey(
                new CreateSurveyRequest("설문제목", "설문설명", List.of(itemDto))
        );

        // 잘못된 ID
        var badDto = new UpdateSurveyItemDto(
                999L, "질문X", "설명X",
                InputType.SHORT_TEXT, true,
                null
        );
        var request = new UpdateSurveyRequest("설문제목", "설문설명", List.of(badDto));

        assertThrows(BadRequestException.class, () -> updater.applyUpdates(survey, request));
    }
}