package com.onboarding.api.dto.response;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.onboarding.api.dto.request.LongTextQuestionReq;
import com.onboarding.api.dto.request.MultiChoiceQuestionReq;
import com.onboarding.api.dto.request.ShortTextQuestionReq;
import com.onboarding.api.dto.request.SingleChoiceQuestionReq;
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
public abstract class QuestionRes {
    protected String questionId;
    protected String title;
    protected String description;
    protected String type;
    protected boolean required;

    protected QuestionRes(String questionId, String title, String description, String type, boolean required) {
        this.questionId = questionId;
        this.title = title;
        this.description = description;
        this.type = type;
        this.required = required;
    }
}
