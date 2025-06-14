package com.INNER_CIRCLE_ICD4.innerCircle.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

public record SurveyUpdateRequest(
        @NotBlank(message = "설문 제목은 필수입니다.")
        String title,

        @NotBlank(message = "설문 설명은 필수입니다.")
        String description,

        @NotEmpty(message = "질문 목록을 하나 이상 포함해야 합니다.")
        @Size(min = 1, max = 10, message = "질문은 1개 이상, 최대 10개까지 허용됩니다.")
        @Valid
        List<@Valid QuestionUpdateRequest> questions
) {}
