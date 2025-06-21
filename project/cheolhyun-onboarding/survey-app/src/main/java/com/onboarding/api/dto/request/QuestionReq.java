package com.onboarding.api.dto.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.onboarding.model.survey.Question;
import lombok.Data;

@Data
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ShortTextQuestionReq.class, name = "SHORT_TEXT"),
        @JsonSubTypes.Type(value = LongTextQuestionReq.class, name = "LONG_TEXT"),
        @JsonSubTypes.Type(value = SingleChoiceQuestionReq.class, name = "SINGLE_CHOICE"),
        @JsonSubTypes.Type(value = MultiChoiceQuestionReq.class, name = "MULTI_CHOICE")
})
public abstract class QuestionReq {
    protected String title;
    protected String type;
    protected String description;
    protected boolean required;

    public abstract Question toDomain();
}
