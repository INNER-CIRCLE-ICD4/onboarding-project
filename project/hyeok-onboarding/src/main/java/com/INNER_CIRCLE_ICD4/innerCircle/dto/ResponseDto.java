package com.INNER_CIRCLE_ICD4.innerCircle.dto;

import java.util.List;
import java.util.UUID;


public record ResponseDto(
        UUID id,
        UUID surveyId,
        List<AnswerDto> answers
) {}