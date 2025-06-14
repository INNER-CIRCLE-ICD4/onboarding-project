package com.group.surveyapp.dto;

import lombok.Data;

import java.util.List;

/**
 * 설문조사 생성/수정 응답 DTO
 * <p>
 * - 설문조사 생성 및 수정 API의 응답값으로 사용.
 * - 설문조사 기본 정보와 항목 정보를 포함.
 * </p>
 */
@Data
public class SurveyResponseDto {
    private Long responseId; // 응답 고유 ID
    private List<SurveyAnswerRequestDto.AnswerDto> answers; // 사용자의 응답 리스트
    private String createdAt; // 응답 생성 시각
}
