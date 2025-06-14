package com.example.hyeongwononboarding.domain.survey.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 설문조사 상세 응답 DTO
 * - 설문조사 조회 시 반환되는 응답 데이터 구조입니다.
 */
@Getter
@Builder
public class SurveyResponse {
    private String surveyId;
    private String title;
    private String description;
    private Integer version;
    private List<QuestionResponse> questions;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime createdAt;
}
