package com.innercircle.onboarding.changzune_onboarding.common;

import com.innercircle.onboarding.changzune_onboarding.common.GlobalExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 400 - 잘못된 요청 (DTO 유효성 실패 등)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GlobalExceptionResponse> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return new ResponseEntity<>(new GlobalExceptionResponse(message, 400), HttpStatus.BAD_REQUEST);
    }

    // 404 - 리소스 없음 (예: 설문 없음 등)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<GlobalExceptionResponse> handleNotFoundException(IllegalArgumentException ex) {
        return new ResponseEntity<>(new GlobalExceptionResponse(ex.getMessage(), 404), HttpStatus.NOT_FOUND);
    }

    // 500 - 서버 내부 에러
    @ExceptionHandler(Exception.class)
    public ResponseEntity<GlobalExceptionResponse> handleException(Exception ex) {
        return new ResponseEntity<>(new GlobalExceptionResponse("서버 오류 발생", 500), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}