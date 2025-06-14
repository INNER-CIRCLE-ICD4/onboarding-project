package com.INNER_CIRCLE_ICD4.innerCircle.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public record AnswerRequest(
        @NotNull(message = "질문 ID는 필수입니다.")
        UUID questionId,
        String text,
        List<UUID> selected
) {}