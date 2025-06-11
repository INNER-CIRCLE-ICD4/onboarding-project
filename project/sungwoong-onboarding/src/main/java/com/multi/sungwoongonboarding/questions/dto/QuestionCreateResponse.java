package com.multi.sungwoongonboarding.questions.dto;

import com.multi.sungwoongonboarding.options.dto.OptionCreateResponse;
import com.multi.sungwoongonboarding.questions.domain.Questions;

import java.util.List;

public record QuestionCreateResponse(Long id, String questionText, int order, boolean isRequired, List<OptionCreateResponse> optionCreateResponseList) {

    public static QuestionCreateResponse fromDomain(Questions question) {
        return new QuestionCreateResponse(
                question.getId(),
                question.getQuestionText(),
                question.getOrder(),
                question.isRequired(),
                question.getOptions().stream().map(OptionCreateResponse::fromDomain).toList()
        );
    }
}
