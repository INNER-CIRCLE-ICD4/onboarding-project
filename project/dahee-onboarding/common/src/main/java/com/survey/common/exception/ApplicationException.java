package com.survey.common.exception;

public class ApplicationException extends RuntimeException {
    private final ErrorCode errorCode;

    public ApplicationException(ErrorCode errorCode, Object... args) {
        super(errorCode.format(args)); // 동적 메시지 적용
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
