// src/main/java/com/INNER_CIRCLE_ICD4/innerCircle/dto/QuestionRequest.java
package com.INNER_CIRCLE_ICD4.innerCircle.dto;

import com.INNER_CIRCLE_ICD4.innerCircle.domain.QuestionType;
import java.util.List;

public record QuestionRequest(
        String title,
        String description,
        QuestionType type,     // ← String → QuestionType
        boolean required,
        List<String> choices
) {}
