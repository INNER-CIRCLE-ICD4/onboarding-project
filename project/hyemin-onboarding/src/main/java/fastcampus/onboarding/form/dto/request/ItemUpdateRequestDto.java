package fastcampus.onboarding.form.dto.request;

import fastcampus.onboarding.form.entity.ItemType;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ItemUpdateRequestDto {
    private Long itemSeq;
    @NotBlank(message = "문항 제목은 필수입니다.")
    private String itemTitle;
    private String itemContent;
    private ItemType itemType;
    private Boolean isRequired;
    private List<OptionUpdateRequestDto> options;
}