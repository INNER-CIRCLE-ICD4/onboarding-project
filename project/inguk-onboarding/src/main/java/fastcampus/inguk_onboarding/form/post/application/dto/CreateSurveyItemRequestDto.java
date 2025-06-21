package fastcampus.inguk_onboarding.form.post.application.dto;

import fastcampus.inguk_onboarding.form.post.domain.Surveys.InputType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateSurveyItemRequestDto(
        @NotBlank(message = "항목 이름은 필수입니다.")
        String name,  // title → name으로 변경

        String description,

        @NotNull(message = "항목 입력 형태는 필수입니다.")
        InputType inputType,

        @NotNull(message = "항목 필수 여부는 필수입니다.")
        Boolean required,

        @NotNull(message = "항목 순서는 필수입니다.")
        Integer order,

        List<String> options
) {


}
