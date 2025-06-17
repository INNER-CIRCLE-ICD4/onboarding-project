package com.survey.soyoung_onboarding.common.exception;

import com.survey.soyoung_onboarding.common.response.ApiResponse;
import com.survey.soyoung_onboarding.common.response.ApiResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponseDto<Object>> handleApiException(ApiException e) {
        return ApiResponse.error(e.getError());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDto<Object>> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("유효성 검사 실패");

        return ApiResponse.error(ApiError.INVALID_PARAMETER.custom(message));
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponseDto<Object>> handleInvalidJson(HttpMessageNotReadableException e) {
        return ApiResponse.error(ApiError.INVALID_PARAMETER.custom("잘못된 요청 형식입니다."));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponseDto<Object>> handleIllegalArgument(IllegalArgumentException e) {
        return ApiResponse.error(ApiError.INVALID_PARAMETER.custom(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto<Object>> handleException(Exception e) {
        log.error(e.getMessage(), e);
        return ApiResponse.error(ApiError.SYSTEM_ERROR);
    }

}
