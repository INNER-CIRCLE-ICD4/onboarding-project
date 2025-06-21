package com.onboarding.api.dto;

import com.onboarding.model.QuestionType;
import com.onboarding.model.survey.Question;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LongTextQuestionReq extends QuestionReq {
    @Override
    public Question toDomain() {
        return Question.of(null, title, description, QuestionType.LONG_TEXT, null, required);
    }
}
