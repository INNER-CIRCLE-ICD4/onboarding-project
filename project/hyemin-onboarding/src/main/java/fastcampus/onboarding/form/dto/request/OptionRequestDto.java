package fastcampus.onboarding.form.dto.request;

import lombok.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class OptionRequestDto {

    @NotBlank(message = "옵션 내용은 필수입니다")
    @Size(max = 200, message = "옵션 내용은 200자를 초과할 수 없습니다")
    private String optionContent;
}