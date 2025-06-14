package com.INNER_CIRCLE_ICD4.innerCircle.dto;

import java.util.List;

public record QuestionRequest(
        String title,
        String description,
        String type,
        boolean required,
        List<String> choices // 단순 텍스트로 선택지 입력받음
) {}
