package onboarding.survey.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import onboarding.survey.domain.InputType;

import java.util.List;

public record SurveyItemDto(
        @NotBlank(message = "항목 이름은 필수입니다.")
        String name,

        @NotBlank(message = "항목 설명은 필수입니다.")
        String description,

        @NotNull(message = "입력 형태는 필수입니다.")
        InputType inputType,

        boolean required,

        @Size(min = 1, message = "선택형 항목은 최소 1개의 선택지가 필요합니다.")
        List<@NotBlank(message = "선택지는 빈 값일 수 없습니다.") String> choices

) {}