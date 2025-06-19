package onboarding.survey.dto;

import java.util.List;

public record CreateSurveyRequest(
        String title,
        String description,
        List<SurveyItemDto> items
) {
}