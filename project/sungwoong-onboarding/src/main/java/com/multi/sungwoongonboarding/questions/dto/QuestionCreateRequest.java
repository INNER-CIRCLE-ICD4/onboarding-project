package com.multi.sungwoongonboarding.questions.dto;

import com.multi.sungwoongonboarding.options.dto.OptionCreateRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class QuestionCreateRequest {
    private final String questionText;
    private final String questionType;
    private final int order;
    private final boolean isRequired;
    private final List<OptionCreateRequest> optionCreateRequests;

}
