package fastcampus.onboarding.form.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OptionUpdateRequestDto {
    private Long optionSeq;
    @NotBlank(message = "옵션 내용은 필수입니다.")
    private String optionContent;

}