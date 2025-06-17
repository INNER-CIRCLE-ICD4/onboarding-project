package com.multi.sungwoongonboarding.responses.dto;

import com.multi.sungwoongonboarding.responses.domain.Answers;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
public class AnswerResponse {

    private final Long id;
    private final Long questionId;
    private final Long optionId;
    private final String answerText;

    public static AnswerResponse fromDomain(Answers answers) {
        return new AnswerResponse(answers.getId(), answers.getQuestionId(), answers.getOptionId(), answers.getAnswerText());
    }

}
