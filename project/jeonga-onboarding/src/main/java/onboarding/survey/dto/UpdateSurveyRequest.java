package onboarding.survey.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UpdateSurveyRequest(

        @NotBlank(message = "제목은 필수입니다.")
        String title,

        String description,

        @NotNull(message = "항목 리스트는 필수입니다.")
        @Size(min = 1, max = 10, message = "항목은 1개 이상, 10개 이하로 입력해야 합니다.")
        @Valid
        List<UpdateSurveyItemDto> items
) {
}