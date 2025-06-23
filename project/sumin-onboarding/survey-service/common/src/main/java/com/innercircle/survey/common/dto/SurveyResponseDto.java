package com.innercircle.survey.common.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyResponseDto {

    @NotNull(message = "설문 ID는 필수입니다.")
    private UUID surveyId;

    @NotEmpty(message = "응답은 최소 1개 이상 있어야 합니다.")
    @Valid
    private List<AnswerDto> answers;
}
