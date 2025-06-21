package com.onboarding.api.dto.request;

import com.onboarding.model.QuestionType;
import com.onboarding.model.survey.Question;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ShortTextQuestionReq extends QuestionReq {
    @Override
    public Question toDomain() {
        return Question.of(null, title, description, QuestionType.SHORT_TEXT, null, required);
    }
}
