package com.INNER_CIRCLE_ICD4.innerCircle.dto;

import com.INNER_CIRCLE_ICD4.innerCircle.domain.QuestionType;

import java.util.List;
import java.util.UUID;

public record QuestionResponse(
        UUID id,
        String title,
        String description,
        QuestionType type,
        boolean required,
        List<ChoiceResponse> choices
) {}
