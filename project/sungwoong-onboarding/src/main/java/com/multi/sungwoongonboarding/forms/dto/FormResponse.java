package com.multi.sungwoongonboarding.forms.dto;

import com.multi.sungwoongonboarding.forms.domain.Forms;
import com.multi.sungwoongonboarding.questions.dto.QuestionResponse;

import java.time.LocalDateTime;
import java.util.List;


public record FormResponse(Long id,
                           String title,
                           String description,
                           List<QuestionResponse> questionResponses,
                           LocalDateTime createdAt,
                           LocalDateTime updatedAt,
                           String userId) {

    public static FormResponse fromDomain(Forms form) {
        return new FormResponse(
                form.getId(),
                form.getTitle(),
                form.getDescription(),
                form.getQuestions().stream().map(QuestionResponse::fromDomain).toList(),
                form.getCreatedAt(),
                form.getUpdatedAt(),
                form.getUserId()
        );
    }

}
