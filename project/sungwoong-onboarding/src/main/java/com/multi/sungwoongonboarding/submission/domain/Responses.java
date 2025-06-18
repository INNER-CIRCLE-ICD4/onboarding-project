package com.multi.sungwoongonboarding.submission.domain;


import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class Responses {

    private final Long id;
    private final Long formId;
    private final String userId;
    private final int formVersion;
    private final LocalDateTime createdAt;
    private final List<Answers> answers;

}
