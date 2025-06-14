package fc.icd.jaehyeononboarding.survey.model.dto;

import fc.icd.jaehyeononboarding.common.model.InputType;
import fc.icd.jaehyeononboarding.survey.model.entity.Question;
import lombok.Data;

@Data
public class QuestionDTO {
    private String label;
    private String description;
    private InputType inputType;
    private boolean required;

    public Question toEntity(Integer index) {
        return Question.create(index, label, description, inputType, required, null);
    }
}
