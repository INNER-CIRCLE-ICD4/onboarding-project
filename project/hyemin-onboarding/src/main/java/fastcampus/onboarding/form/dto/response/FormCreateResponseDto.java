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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FormCreateResponseDto {
    private Long formSeq;
    private String formTitle;
    private String formContent;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ItemResponseDto> items;
}