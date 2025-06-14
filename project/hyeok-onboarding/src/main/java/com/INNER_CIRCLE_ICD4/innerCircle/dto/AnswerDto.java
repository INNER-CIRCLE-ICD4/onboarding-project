package com.INNER_CIRCLE_ICD4.innerCircle.dto;

import com.INNER_CIRCLE_ICD4.innerCircle.domain.Answer;
import com.INNER_CIRCLE_ICD4.innerCircle.domain.AnswerChoice;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record AnswerDto(
        UUID questionId,
        String text,
        List<String> selected
) {}

