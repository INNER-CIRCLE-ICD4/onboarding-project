package com.multi.sungwoongonboarding.submission.dto;

import com.multi.sungwoongonboarding.submission.domain.Responses;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Builder
@RequiredArgsConstructor
public class ResponseSheet {

    private final Long id;
    private final Long formId;
    private final String userId;
    private final List<AnswerResponse> answerResponses;

    public static ResponseSheet fromDomain(Responses responses) {
        return new ResponseSheet(responses.getId(), responses.getFormId(), responses.getUserId(), responses.getAnswers().stream().map(AnswerResponse::fromDomain).toList());
    }
}
