package com.survey.soyoung_onboarding.common.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponseDto<T> {
    private boolean success;
    private T data;
    private ErrorInfo error;

    public ApiResponseDto(T data) {
        this.success = true;
        this.data = data;
    }

    public ApiResponseDto(int code, String message) {
        this.success = false;
        this.error = new ErrorInfo(code, message);
    }

    @Getter
    @Setter
    public static class ErrorInfo {
        private int code;
        private String message;

        public ErrorInfo(int code, String message) {
            this.code = code;
            this.message = message;
        }
    }
}

