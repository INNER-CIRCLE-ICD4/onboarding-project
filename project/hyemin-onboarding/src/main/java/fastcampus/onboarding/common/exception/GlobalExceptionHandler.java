package fastcampus.onboarding.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.error("CustomException 발생: {}", e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(errorCode), HttpStatus.valueOf(errorCode.getStatus()));
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("처리되지 않은 예외 발생: {}", e.getMessage(), e);
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<>(new ErrorResponse(errorCode), HttpStatus.INTERNAL_SERVER_ERROR);
    }
} 