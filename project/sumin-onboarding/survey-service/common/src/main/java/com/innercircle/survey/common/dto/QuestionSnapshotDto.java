package com.innercircle.survey.common.dto;

import java.util.UUID;

public record QuestionSnapshotDto(
        UUID id,
        String title,
        String description,
        String type,
        boolean required
) {}
