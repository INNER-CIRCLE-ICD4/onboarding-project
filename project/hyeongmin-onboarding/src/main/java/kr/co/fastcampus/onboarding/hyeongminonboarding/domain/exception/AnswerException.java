package kr.co.fastcampus.onboarding.hyeongminonboarding.domain.exception;

import kr.co.fastcampus.onboarding.hyeongminonboarding.global.exception.BaseException;
import kr.co.fastcampus.onboarding.hyeongminonboarding.global.exception.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class AnswerException extends BaseException {
    public AnswerException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public ErrorCode getErrorCode() {
        return super.getErrorCode();
    }

    @Override
    public HttpStatus getStatus() {
        return super.getStatus();
    }
}
