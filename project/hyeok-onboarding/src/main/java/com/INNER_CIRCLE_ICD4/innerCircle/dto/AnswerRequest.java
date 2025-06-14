package com.INNER_CIRCLE_ICD4.innerCircle.dto;

import java.util.List;
import java.util.UUID;

public record AnswerRequest(
        UUID questionId,
        String textValue,                // 주관식 응답
        List<UUID> selectedChoiceIds     // 객관식 응답 (단/복수 선택)
) {}
