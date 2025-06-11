package com.multi.sungwoongonboarding.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    //공통 에러
    INVALID_INPUT("E-001", HttpStatus.BAD_REQUEST, "Invalid input provided"),
    RESOURCE_NOT_FOUND("E-002", HttpStatus.NOT_FOUND, "Requested resource not found"),
    INTERNAL_SERVER_ERROR("E-003", HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred"),
    VALIDATION_ERROR("E-004", HttpStatus.BAD_REQUEST, "Form validation error"),

    //설문지

    //질문

    //질문 옵션

    ;
    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(String code, HttpStatus httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
