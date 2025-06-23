package fastcampus.onboarding.form.dto.request;

import fastcampus.onboarding.form.entity.ItemType;
import lombok.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ItemRequestDto {

    @NotBlank(message = "항목 제목은 필수입니다")
    @Size(max = 1000, message = "항목 제목은 1000자를 초과할 수 없습니다")
    private String itemTitle;

    @NotBlank(message = "항목 내용은 필수입니다")
    @Size(max = 1000, message = "항목 내용은 1000자를 초과할 수 없습니다")
    private String itemContent;

    @NotNull(message = "항목 유형은 필수입니다")
    private ItemType itemType;

    @NotNull(message = "필수 여부는 필수입니다")
    private Boolean isRequired;

    @Valid
    private List<OptionRequestDto> options = new ArrayList<>();


    // 유효성 검증을 위한 메서드 (선택형 질문인 경우 옵션 필요)
    public boolean isChoiceTypeWithOptions() {
        return (itemType == ItemType.SINGLE_CHOICE || itemType == ItemType.MULTIPLE_CHOICE)
                && (options == null || options.isEmpty());
    }

    public boolean isTextTypeWithOptions() {
        return (itemType == ItemType.SHORT_ANSWER || itemType == ItemType.PARAGRAPH)
                && (options != null && !options.isEmpty());
    }
}