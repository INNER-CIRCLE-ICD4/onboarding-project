package com.innercircle.survey.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SurveyResponseDetailDto {
    private UUID id;
    private LocalDateTime submittedAt;
    private List<QuestionSnapshotDto> snapshot;
    private List<AnswerDto> answers;
}
