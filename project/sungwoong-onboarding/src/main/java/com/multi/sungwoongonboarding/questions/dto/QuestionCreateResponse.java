package com.multi.sungwoongonboarding.questions.dto;

import com.multi.sungwoongonboarding.options.dto.OptionCreateResponse;
import com.multi.sungwoongonboarding.questions.domain.Questions;

import java.util.List;

public record QuestionCreateResponse(Long id,
                                     String questionText,
                                     boolean isRequired,
                                     int version,
                                     boolean deleted,
                                     List<OptionCreateResponse> optionCreateResponses) {

    public static QuestionCreateResponse fromDomain(Questions question) {
        return new QuestionCreateResponse(
                question.getId(),
                question.getQuestionText(),
                question.isRequired(),
                question.getVersion(),
                question.isDeleted(),
                question.getOptions().stream().map(OptionCreateResponse::fromDomain).toList()
        );
    }
}
