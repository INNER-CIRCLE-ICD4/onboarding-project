package com.survey.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiErrorResponse {
    private String code;      // 예: "SURVEY_NOT_FOUND"
    private String message;   // 예: "존재하지 않는 설문입니다: 123"
}
