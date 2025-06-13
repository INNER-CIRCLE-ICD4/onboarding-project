package kr.co.fastcampus.onboarding.hyeongminonboarding.global.exception;

import kr.co.fastcampus.onboarding.hyeongminonboarding.global.exception.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public abstract class BaseException extends RuntimeException {
    private final ErrorCode errorCode;

    public BaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public HttpStatus getStatus() {
        return errorCode.getStatus();
    }
}

