package com.survey.common.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SurveyResponseDto {
    private Long surveyId;
    private String seriesCode;
    private int version;
    private String title;
    private LocalDateTime createdAt;
}
