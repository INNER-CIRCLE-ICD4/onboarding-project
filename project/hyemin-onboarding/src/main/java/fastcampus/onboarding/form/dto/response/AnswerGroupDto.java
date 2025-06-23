package fastcampus.onboarding.form.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class AnswerGroupDto {
    private Long responseSeq;
    private List<AnswerItemDto> items;
}
