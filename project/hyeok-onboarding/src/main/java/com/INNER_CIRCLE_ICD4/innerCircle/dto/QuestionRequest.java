package com.INNER_CIRCLE_ICD4.innerCircle.dto;

import com.INNER_CIRCLE_ICD4.innerCircle.domain.QuestionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record QuestionRequest(
        @NotBlank(message = "질문 제목은 필수입니다.")
        String title,

        String description,

        @NotNull(message = "질문 타입은 필수입니다.")
        QuestionType type,

        boolean required,

        List<String> choices
) {}
