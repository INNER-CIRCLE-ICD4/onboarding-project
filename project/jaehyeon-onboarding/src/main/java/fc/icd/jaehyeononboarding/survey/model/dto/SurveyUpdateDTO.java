package fc.icd.jaehyeononboarding.survey.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SurveyUpdateDTO extends SurveyCreateDTO {
    private Long surveyId;
    private int version;
}
