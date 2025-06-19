package fc.icd.jaehyeononboarding.answer.model.dto;

import fc.icd.jaehyeononboarding.common.model.InputType;
import lombok.Data;

import java.util.List;

@Data
public class AnswerWithQuestionDTO<T> {
    private String label;
    private String description;
    private InputType inputType;
    private boolean required;
    private List<String> options;
    private T content;

}
