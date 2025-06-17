package com.survey.common.exception;

public class ApplicationException extends RuntimeException {
    private final ErrorCode code;

    public ApplicationException(ErrorCode code, Object... args) {
        super(code.format(args));
        this.code = code;
    }

    public ErrorCode getCode() {
        return code;
    }
}
