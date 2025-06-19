package fastcampus.inguk_onboarding.form.response.application.dto;

import java.util.List;

public record ResponseSurveyRequestDto(
        List<ResponseSurveyItemRequestDto> answers
) {
}
