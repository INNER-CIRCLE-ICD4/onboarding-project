package com.survey.soyoung_onboarding.common.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

    private final ApiError error;

    public ApiException(ApiError error) {
        super(error.getMessage());
        this.error = error;
    }

    public ApiException(ApiError error, Throwable cause) {
        super(error.getMessage(), cause);
        this.error = error;
    }

}
