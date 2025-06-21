package onboarding.survey.dto;

import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import onboarding.survey.domain.InputType;

public record UpdateSurveyItemDto(
        Long id,  // 기존 항목이면 ID, 새 항목이면 null

        @NotBlank
        String name,

        @NotBlank
        String description,

        @NotNull
        InputType inputType,

        boolean required,

        List<@NotBlank String> choices
) {}
