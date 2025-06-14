package com.INNER_CIRCLE_ICD4.innerCircle.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public record ResponseRequest(
        @NotNull(message = "설문 ID는 필수입니다.")
        UUID surveyId,

        @NotEmpty(message = "답변을 하나 이상 포함해야 합니다.")
        @Valid
        List<AnswerRequest> answers
) {}
