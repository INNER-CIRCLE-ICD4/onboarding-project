package fastcampus.onboarding.form.dto.response;

import fastcampus.onboarding.form.dto.request.ItemRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class FormCreateResponseDto {
    private Long formSeq;             // 설문지 고유 번호
    private String formTitle;         // 설문지 제목
    private String formContent;       // 설문지 설명
    private LocalDateTime createdAt;  // 생성 일시
    private LocalDateTime updatedAt;
    private List<ItemRequestDto> items;
}
