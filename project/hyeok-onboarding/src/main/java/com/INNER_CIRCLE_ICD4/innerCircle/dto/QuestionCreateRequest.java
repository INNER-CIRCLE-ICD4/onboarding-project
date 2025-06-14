package com.INNER_CIRCLE_ICD4.innerCircle.dto;

import java.util.List;
import com.INNER_CIRCLE_ICD4.innerCircle.domain.QuestionType;

public record QuestionCreateRequest(
        String title,
        String description,
        QuestionType type,
        boolean required,
        List<ChoiceCreateRequest> choices
) {}

