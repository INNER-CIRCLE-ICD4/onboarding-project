package com.multi.sungwoongonboarding.submission.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class SubmissionSearchRequest {

    private final String questionText;
    private final String answerText;

}
