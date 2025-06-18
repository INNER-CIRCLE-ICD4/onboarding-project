package com.innercircle.survey.common.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class QuestionUpdateDto {
    // 기존 질문 수정 시 필요. 새로 추가된 질문은 null 가능
    private UUID questionId;

    @NotNull(message = "질문 제목은 필수입니다.")
    private String title;

    private String description;

    @NotNull(message = "질문 타입은 필수입니다.")
    private String type;

    private boolean required;

    // 단/복수 선택일 때
    private List<String> options;

}
