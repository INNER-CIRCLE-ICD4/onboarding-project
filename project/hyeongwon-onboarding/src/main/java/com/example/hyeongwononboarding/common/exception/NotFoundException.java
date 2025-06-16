package com.example.hyeongwononboarding.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 요청한 리소스를 찾을 수 없을 때 발생하는 예외입니다.
 * HTTP 404 상태 코드로 응답합니다.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
