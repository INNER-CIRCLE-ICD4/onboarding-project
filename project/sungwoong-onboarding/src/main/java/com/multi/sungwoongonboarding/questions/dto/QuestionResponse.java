package com.multi.sungwoongonboarding.questions.dto;

import com.multi.sungwoongonboarding.options.dto.OptionResponse;
import com.multi.sungwoongonboarding.questions.domain.Questions;

import java.util.List;

public record QuestionResponse(Long id,
                               String questionText,
                               Questions.QuestionType questionType,
                               boolean isRequired,
                               boolean deleted,
                               List<OptionResponse> optionResponses) {

    public static QuestionResponse fromDomain(Questions question) {
        return new QuestionResponse(
                question.getId(),
                question.getQuestionText(),
                question.getQuestionType(),
                question.isRequired(),
                question.isDeleted(),
                question.getOptions().stream().map(OptionResponse::fromDomain).toList()
        );
    }
}
