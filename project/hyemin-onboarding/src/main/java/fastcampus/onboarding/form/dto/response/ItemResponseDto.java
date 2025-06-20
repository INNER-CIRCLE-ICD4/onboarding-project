package fastcampus.onboarding.form.dto.response;

import fastcampus.onboarding.form.dto.request.OptionRequestDto;
import fastcampus.onboarding.form.entity.ItemType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ItemResponseDto {
    private String itemTitle;
    private String itemContent;
    private ItemType itemType;
    private Boolean isRequired;
    private List<OptionResponseDto> options = new ArrayList<>();

}