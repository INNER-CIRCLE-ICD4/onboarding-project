package com.survey.common.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ResponseDto {
    private Long responseId;
    private LocalDateTime submittedAt;
}
