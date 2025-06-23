package fastcampus.onboarding.form.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class AnswerDto {
    private Long formSeq;
    private String formTitle;
    private String formContent;
    private List<AnswerGroupDto> answerItems;

    public void answerItems(List<AnswerGroupDto> answerItems) {
        this.answerItems = answerItems;

    }
}
