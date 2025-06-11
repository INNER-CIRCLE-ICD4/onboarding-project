package com.multi.sungwoongonboarding.common.exception.handler;

import com.multi.sungwoongonboarding.common.dto.FieldErrorDto;
import com.multi.sungwoongonboarding.common.dto.ResponseDto;
import com.multi.sungwoongonboarding.common.exception.ErrorCode;
import com.multi.sungwoongonboarding.common.util.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseDto> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(ResponseUtil.error(ErrorCode.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto<List<FieldErrorDto>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        ResponseDto<List<FieldErrorDto>> error = ResponseUtil.error(ErrorCode.VALIDATION_ERROR, e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> FieldErrorDto.builder()
                        .field(fieldError.getField())
                        .message(fieldError.getDefaultMessage())
                        .build())
                .toList());

        return ResponseEntity.badRequest().body(error);
    }

}
