package com.example.hyeongwononboarding.domain.survey.dto.response;

import lombok.*;

/**
 * 설문 응답 제출 결과 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmitSurveyResponseResponse {
    private String responseId;
    private String message;
}
