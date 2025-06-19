package com.INNER_CIRCLE_ICD4.innerCircle.dto;

import java.util.List;

public record SurveyCreateRequest(
        String title,
        String description,
        List<QuestionRequest> questions
) {}

