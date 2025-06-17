package com.survey.common.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 설문 응답(서버 → 클라이언트) 응답 DTO
 *
 * 설문 응답 리스트 조회용(여러 명의 답변/문항별 답변 결과 반환)
 */
@Getter
@Builder
public class SurveyResponseDto {
    private Long surveyId;
    private String seriesCode;
    private int version;
    private String title;
    private LocalDateTime createdAt;

}
