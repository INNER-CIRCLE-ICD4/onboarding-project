package com.INNER_CIRCLE_ICD4.innerCircle.dto;

import java.util.List;
import com.INNER_CIRCLE_ICD4.innerCircle.domain.QuestionType;

public record QuestionRequest(
        String title,
        String description,
        QuestionType type,   // String → QuestionType 으로 변경
        boolean required,
        List<String> choices
) {}
