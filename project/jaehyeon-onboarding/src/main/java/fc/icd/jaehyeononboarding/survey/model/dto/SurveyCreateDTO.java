package fc.icd.jaehyeononboarding.survey.model.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

import java.util.List;

@Data
public class SurveyCreateDTO {
    private String name;
    private String description;

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.PROPERTY,
            property = "input_type",
            visible = true
    )
    @JsonSubTypes({
            @JsonSubTypes.Type(value = SelectQuestionDTO.class, names = {"RADIO", "radio", "CHECKBOX", "checkbox"}),
            @JsonSubTypes.Type(value = QuestionDTO.class, names = {"TEXT", "text", "LONG_TEXT", "long_text"}),
    })
    private List<QuestionDTO> questions;
}
