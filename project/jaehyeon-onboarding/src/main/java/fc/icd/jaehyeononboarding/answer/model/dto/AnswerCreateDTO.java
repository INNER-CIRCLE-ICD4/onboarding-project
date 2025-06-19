package fc.icd.jaehyeononboarding.answer.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class AnswerCreateDTO {
    private Long questionGroupId;
    @JsonProperty("answers_with_question")
    private List<AnswerWithQuestionDTO<?>> answersWithQuestion;
}
