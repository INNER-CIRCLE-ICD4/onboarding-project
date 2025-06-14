package com.INNER_CIRCLE_ICD4.innerCircle.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record SurveyRequest(
        @NotBlank(message = "설문 제목은 필수입니다.")
        String title,

        @NotBlank(message = "설문 설명은 필수입니다.")
        String description,

        @NotEmpty(message = "질문 목록은 하나 이상 포함해야 합니다.")
        @Valid
        List<@Valid QuestionRequest> questions
) {}
