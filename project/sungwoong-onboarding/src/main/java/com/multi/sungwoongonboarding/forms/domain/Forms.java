package com.multi.sungwoongonboarding.forms.domain;

import com.multi.sungwoongonboarding.forms.dto.FormCreateRequest;
import com.multi.sungwoongonboarding.questions.domain.Questions;
import com.multi.sungwoongonboarding.questions.dto.QuestionCreateRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class Forms {
    private final Long id;
    private final String title;
    private final String description;
    private final List<Questions> questions;

    public static Forms form(FormCreateRequest formCreateRequest) {
        return Forms.builder()
                .title(formCreateRequest.getTitle())
                .description(formCreateRequest.getDescription())
                .questions(
                        formCreateRequest.getQuestionCreateRequests().stream().map(Questions::from).toList()
                )
                .build();
    }
}
