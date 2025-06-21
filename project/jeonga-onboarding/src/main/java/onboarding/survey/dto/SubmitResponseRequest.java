package onboarding.survey.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SubmitResponseRequest(
        @NotNull(message = "문항 ID는 필수입니다.")
        Long surveyItemId,

        @NotBlank(message = "답변은 필수입니다.")
        String answer
) {}