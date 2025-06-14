package fastcampus.onboarding.form.dto.response;

import fastcampus.onboarding.form.dto.request.ItemRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FormCreateResponseDto {

    @NotBlank(message = "설문조사 제목은 필수입니다")
    @Size(max = 200, message = "설문조사 제목은 200자를 초과할 수 없습니다")
    private String formTitle;

    @Size(max = 1000, message = "설문조사 설명은 1000자를 초과할 수 없습니다")
    private String formContent;

    @NotEmpty(message = "최소 1개 이상의 설문 항목이 필요합니다")
    @Size(min = 1, max = 10, message = "설문 항목은 1개에서 10개까지 가능합니다")
    @Valid
    private List<ItemRequestDto> items;
}