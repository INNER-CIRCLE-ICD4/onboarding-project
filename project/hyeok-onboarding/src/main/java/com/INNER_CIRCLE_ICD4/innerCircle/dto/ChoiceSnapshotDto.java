package com.INNER_CIRCLE_ICD4.innerCircle.dto;

import com.INNER_CIRCLE_ICD4.innerCircle.domain.Choice;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ChoiceSnapshotDto {
    private UUID id;
    private String text;
    private int choiceIndex;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ChoiceSnapshotDto from(Choice choice) {
        return ChoiceSnapshotDto.builder()
                .id(choice.getId())
                .text(choice.getText())
                .choiceIndex(choice.getChoiceIndex())
                .createdAt(choice.getCreatedAt())
                .updatedAt(choice.getUpdatedAt())
                .build();
    }
}
