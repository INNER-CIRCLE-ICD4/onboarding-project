package com.innercircle.onboarding.common.exceptions;

import com.innercircle.onboarding.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 공통 예외 처리
 */
@Slf4j
@RestControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(CommonException.class)
    public ResponseEntity<ApiResponse.Exception> commonExceptionHandler(HttpServletRequest request, CommonException commonException) {
        return ResponseEntity
                .status(commonException.getResponse().getHttpsStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        ApiResponse.Exception
                                .builder()
                                .path(request.getServletPath())
                                .commonException(commonException)
                                .build()
                );
    }

}
