package com.multi.sungwoongonboarding.submission.dto;

import com.multi.sungwoongonboarding.submission.domain.Answers;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
public class AnswerResponse {

    // todo 질문_답변 구조 수정 필요 - 질문 내용
    private final Long id;
    private final Long questionId;
    private final Long optionId;
    private final String answerText;

    public static AnswerResponse fromDomain(Answers answers) {
        return new AnswerResponse(answers.getId(), answers.getQuestionId(), answers.getOptionId(), answers.getAnswerText());
    }

}
