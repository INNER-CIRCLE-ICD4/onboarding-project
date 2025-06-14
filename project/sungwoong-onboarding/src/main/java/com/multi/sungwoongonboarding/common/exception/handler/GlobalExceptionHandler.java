package com.multi.sungwoongonboarding.common.exception.handler;

import com.multi.sungwoongonboarding.common.exception.dto.ErrorResponseDto;
import com.multi.sungwoongonboarding.common.exception.dto.FieldErrorDto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgumentException(IllegalArgumentException e) {

        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                .errorCode("400")
                .errorMessage(e.getMessage())
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto<FieldErrorDto>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        ErrorResponseDto<FieldErrorDto> errorResponse = ErrorResponseDto.<FieldErrorDto>builder()
                .errorCode("400")
                .errorMessage("Validation failed")
                .errorDetails(e.getBindingResult().getFieldErrors().stream()
                        .map(fieldError -> FieldErrorDto.builder()
                                .field(fieldError.getField())
                                .message(fieldError.getDefaultMessage())
                                .build())
                        .toList())
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

}
