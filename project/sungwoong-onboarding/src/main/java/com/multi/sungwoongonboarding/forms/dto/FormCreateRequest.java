package com.multi.sungwoongonboarding.forms.dto;

import com.multi.sungwoongonboarding.questions.dto.QuestionCreateRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class FormCreateRequest {
    private final String title;
    private final String description;
    private final List<QuestionCreateRequest> questionCreateRequests;

}
