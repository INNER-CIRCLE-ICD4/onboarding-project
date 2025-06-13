package com.multi.sungwoongonboarding.forms.dto;

import com.multi.sungwoongonboarding.forms.domain.Forms;
import com.multi.sungwoongonboarding.questions.domain.Questions;
import com.multi.sungwoongonboarding.questions.dto.QuestionCreateResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


public record FormCreateResponse (Long id,
                                  String title,
                                  String description,
                                  List<QuestionCreateResponse> questionCreateResponses,
                                  LocalDateTime createdAt,
                                  LocalDateTime updatedAt,
                                  String userId) {

    public static FormCreateResponse fromDomain(Forms form) {
        return new FormCreateResponse(
                form.getId(),
                form.getTitle(),
                form.getDescription(),
                form.getQuestions().stream().map(QuestionCreateResponse::fromDomain).toList(),
                form.getCreatedAt(),
                form.getUpdatedAt(),
                form.getUserId()
        );
    }

}
