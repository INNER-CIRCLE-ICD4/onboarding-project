package com.INNER_CIRCLE_ICD4.innerCircle.dto;

import java.util.List;
import java.util.UUID;

public record AnswerDto(
        UUID questionId,
        String text,
        List<UUID> selected
) {}
