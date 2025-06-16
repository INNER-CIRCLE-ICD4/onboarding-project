package com.INNER_CIRCLE_ICD4.innerCircle.exception;

import com.INNER_CIRCLE_ICD4.innerCircle.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessException ex) {
        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.of(ex.getMessage(), 400));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity
                .status(404)
                .body(ErrorResponse.of(ex.getMessage(), 404));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatus(ResponseStatusException ex) {
        String message = ex.getReason() != null ? ex.getReason() : "요청 처리 중 오류가 발생했습니다.";
        int status = ex.getStatusCode().value();
        return ResponseEntity
                .status(status)
                .body(ErrorResponse.of(message, status));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnknown(Exception ex) {
        ex.printStackTrace(); // 로그 확인용
        return ResponseEntity
                .internalServerError()
                .body(ErrorResponse.of("알 수 없는 에러가 발생했습니다.", 500));
    }
}
