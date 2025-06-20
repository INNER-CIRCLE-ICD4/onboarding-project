package fastcampus.onboarding.form.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AnswerRequestDto {
    List<ItemAnswerDto> answers ;

    @Getter
    @Builder
    public static class ItemAnswerDto {
        private Long itemSeq;
        private String textValue;
        private List<Long> optionSeqs;
    }
}
