package fastcampus.onboarding.form.dto.request;

import fastcampus.onboarding.form.entity.ItemType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Builder
@Setter
public class FormUpdateRequestDto {
    @NotBlank(message = "제목은 필수입니다.")
    private String formTitle;
    @NotBlank(message = "내용은 비어있을 수 없습니다.")
    private String formContent;
    @NotEmpty(message = "문항은 최소 하나 이상이어야 합니다.")
    @Size(max = 10, message = "항목은 최대 10개까지만 허용됩니다.")
    private List<ItemUpdateRequestDto> items;


}