package com.INNER_CIRCLE_ICD4.innerCircle.dto;

import java.util.List;
import java.util.UUID;

public record SurveyResponse(
        UUID id,
        String title,
        String description,
        int version,
        List<QuestionResponse> questions
) {}