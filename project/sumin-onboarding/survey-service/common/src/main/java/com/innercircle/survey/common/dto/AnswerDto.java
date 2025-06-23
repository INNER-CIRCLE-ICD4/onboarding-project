package com.innercircle.survey.common.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerDto {

    @NotNull(message = "질문 ID는 필수입니다.")
    private String questionId;

    @NotNull(message = "응답 값은 필수입니다.")
    private String answer;
}
