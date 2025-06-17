package fastcampus.inguk_onboarding.form.post.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UpdateSurveyRequestDto(
        // URL 경로에서 ID를 받으므로 DTO에서는 선택적
        Long id,
        
        @NotBlank(message = "설문조사 이름은 필수입니다.")
        String name,  // title → name으로 변경
        String description,
        String state,  // state 필드 추가

        @NotNull(message ="설문 받을 항목은 필수입니다.")
        @Size(min=1, max=10, message="설문 받을 항목은 1개 ~ 10개 까지 포함할 수 있습니다.")
        List<CreateSurveyItemRequestDto> items

) implements SurveyRequestDto {


}
