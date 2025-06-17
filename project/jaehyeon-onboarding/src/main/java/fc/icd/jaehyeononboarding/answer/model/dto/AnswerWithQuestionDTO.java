package fc.icd.jaehyeononboarding.answer.model.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import fc.icd.jaehyeononboarding.answer.model.AnswerWithQuestionDeserializer;
import fc.icd.jaehyeononboarding.survey.model.dto.QuestionDTO;
import fc.icd.jaehyeononboarding.survey.model.dto.SelectQuestionDTO;
import lombok.Data;

@Data
@JsonDeserialize(using = AnswerWithQuestionDeserializer.class)
public class AnswerWithQuestionDTO<T> {
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
    private QuestionDTO question;

    private T content;

}
