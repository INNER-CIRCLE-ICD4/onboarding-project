package com.INNER_CIRCLE_ICD4.innerCircle.dto;

import com.INNER_CIRCLE_ICD4.innerCircle.domain.QuestionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record QuestionRequest(
        @NotBlank(message = "질문 제목은 필수입니다.")
        String title,

        @NotBlank(message = "질문 설명은 필수입니다.")
        String description,

        @NotNull(message = "질문 타입은 필수입니다.")
        QuestionType type,

        @NotNull(message = "필수 여부(required) 필드는 true/false로 지정해야 합니다.")
        Boolean required,

        @NotNull(message = "choices 필드는 null일 수 없습니다.")
        List<@NotBlank(message = "선택지 텍스트는 비어있을 수 없습니다.") String> choices

) {}
