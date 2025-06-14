package com.group.surveyapp.dto;

import lombok.Data;

import java.util.List;

/**
 * 설문조사 응답 조회 응답 DTO
 * <p>
 * - 설문조사 응답 조회 API(/api/surveys/{surveyId}/responses) 응답값에 사용.
 * - 설문 항목별 응답 데이터 및 응답 메타 정보를 포함.
 * </p>
 */
@Data
public class SurveyAnswerResponseDto {
    private Long id; // 설문 ID
    private String title; // 설문 제목
    private String description; // 설문 설명
    private List<SurveyCreateRequestDto.QuestionDto> questions; // 질문 및 응답 정보
}
