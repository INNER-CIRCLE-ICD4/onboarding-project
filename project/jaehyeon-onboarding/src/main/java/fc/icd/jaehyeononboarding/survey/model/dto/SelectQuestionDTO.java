package fc.icd.jaehyeononboarding.survey.model.dto;

import fc.icd.jaehyeononboarding.survey.model.entity.Question;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SelectQuestionDTO extends QuestionDTO {
    private List<String> options;

    @Override
    public Question toEntity(Integer index) {
        Question entity = super.toEntity(index);
        if (options != null) {
            entity.setOptions(new ArrayList<>(options));
        }
        return entity;
    }
}
