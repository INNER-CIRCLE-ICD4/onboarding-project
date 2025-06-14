package com.INNER_CIRCLE_ICD4.innerCircle.dto;

import java.util.List;
import java.util.UUID;

public record ResponseRequest(
        UUID surveyId,
        List<AnswerRequest> answers
) {}
