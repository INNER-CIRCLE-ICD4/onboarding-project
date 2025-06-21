package com.onboarding.api.dto.request;

import com.onboarding.model.QuestionType;
import com.onboarding.model.survey.Options;
import com.onboarding.model.survey.Question;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
public class MultiChoiceQuestionReq extends QuestionReq {
    private List<OptionsReq> options;

    @Override
    public Question toDomain() {
        List<String> optionList = options.stream()
                .map(OptionsReq::getLabel)
                .collect(Collectors.toList());

        return Question.of(null, title, description, QuestionType.MULTI_CHOICE, new Options(optionList), required);
    }
}
