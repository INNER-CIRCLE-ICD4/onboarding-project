package com.INNER_CIRCLE_ICD4.innerCircle.dto;

import com.INNER_CIRCLE_ICD4.innerCircle.domain.Survey;
import com.INNER_CIRCLE_ICD4.innerCircle.dto.QuestionSnapshotDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Builder
public class SurveySnapshotDto {
    private UUID id;
    private String title;
    private String description;
    private int version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<QuestionSnapshotDto> questions;

    public static SurveySnapshotDto from(Survey survey) {
        return SurveySnapshotDto.builder()
                .id(survey.getId())
                .title(survey.getTitle())
                .description(survey.getDescription())
                .version(survey.getVersion())
                .createdAt(survey.getCreatedAt())
                .updatedAt(survey.getUpdatedAt())
                .questions(
                        survey.getQuestions().stream()
                                .map(QuestionSnapshotDto::from)
                                .collect(Collectors.toList())
                )
                .build();
    }
}
