package com.innercircle.survey.common.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyUpdateDto {
    @NotNull(message = "설문 제목은 필수입니다.")
    private String title;

    private String description;

    @Size(min = 1, max = 10, message = "질문은 1개 이상 10개 이하입니다.")
    @Valid
    private List<QuestionUpdateDto> questions;;
}
