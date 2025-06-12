package fastcampus.onboarding.common.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private final String code;
    private final String message;
    private final int status;

    public ErrorResponse(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.status = errorCode.getStatus();
    }

    public ErrorResponse(ErrorCode errorCode, String message) {
        this.code = errorCode.getCode();
        this.message = message;
        this.status = errorCode.getStatus();
    }
} 