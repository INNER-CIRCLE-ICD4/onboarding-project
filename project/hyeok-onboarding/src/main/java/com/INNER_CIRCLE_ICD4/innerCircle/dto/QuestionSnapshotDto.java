package com.INNER_CIRCLE_ICD4.innerCircle.dto;

import com.INNER_CIRCLE_ICD4.innerCircle.domain.Question;
import com.INNER_CIRCLE_ICD4.innerCircle.domain.QuestionType;
import com.INNER_CIRCLE_ICD4.innerCircle.dto.ChoiceSnapshotDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Builder
public class QuestionSnapshotDto {
    private UUID id;
    private String title;
    private String description;
    private QuestionType type;
    private boolean required;
    private boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ChoiceSnapshotDto> choices;

    public static QuestionSnapshotDto from(Question question) {
        return QuestionSnapshotDto.builder()
                .id(question.getId())
                .title(question.getTitle())
                .description(question.getDescription())
                .type(question.getType())
                .required(question.isRequired())
                .isDeleted(question.isDeleted())
                .createdAt(question.getCreatedAt())
                .updatedAt(question.getUpdatedAt())
                .choices(
                        question.getChoices().stream()
                                .map(ChoiceSnapshotDto::from)
                                .collect(Collectors.toList())
                )
                .build();
    }
}
