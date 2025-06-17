package com.survey.soyoung_onboarding.common.exception;

import lombok.Getter;

@Getter
public enum ApiError {
    REQUIRED_PARAMETER(101, "필수 파라미터가 누락",400),
    INVALID_PARAMETER(102, "유효하지 않은 파라미터",400),
    SYSTEM_ERROR(500, "시스템 오류",500),
    ;

    private final int code;
    private final String message;
    private final int status;

    ApiError(int code, String message, int status){
        this.code = code;
        this.message = message;
        this.status = status;
    }

    public ApiErrorDetail custom(String message) {
        return new ApiErrorDetail(this.code, message, this.status);
    }
}
