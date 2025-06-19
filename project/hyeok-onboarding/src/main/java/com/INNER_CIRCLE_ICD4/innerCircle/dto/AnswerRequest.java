package com.INNER_CIRCLE_ICD4.innerCircle.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public record AnswerRequest(
        @NotNull(message = "질문 ID는 필수입니다.")
        UUID questionId,

        // 단답/장문형에서 사용하는 텍스트 응답
        String text,

        // 객관식(단일/다중 선택)에서 사용하는 선택지 ID 목록
        List<UUID> selectedOptions
) {}
