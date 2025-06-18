package com.multi.sungwoongonboarding.submission.dto;

import com.multi.sungwoongonboarding.submission.domain.Submission;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Builder
@RequiredArgsConstructor
public class SubmissionResponse {

    private final Long id;
    private final Long formId;
    private final String userId;
    private final List<AnswerResponse> answerResponses;

    public static SubmissionResponse fromDomain(Submission submission) {
        return new SubmissionResponse(submission.getId(), submission.getFormId(), submission.getUserId(), submission.getAnswers().stream().map(AnswerResponse::fromDomain).toList());
    }
}
