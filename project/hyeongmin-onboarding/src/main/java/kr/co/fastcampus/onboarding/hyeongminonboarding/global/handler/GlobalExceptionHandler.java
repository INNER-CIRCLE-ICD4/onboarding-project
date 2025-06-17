package kr.co.fastcampus.onboarding.hyeongminonboarding.global.handler;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.fastcampus.onboarding.hyeongminonboarding.global.dto.response.ErrorResponse;
import kr.co.fastcampus.onboarding.hyeongminonboarding.global.exception.BaseException;
import kr.co.fastcampus.onboarding.hyeongminonboarding.global.exception.enums.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * @Valid 에러 처리
     * 프론트에서 Form 데이터 유효성 체크를 쉽게 하기 위해 Request 에서 정의한 유효성에 위배한 필드를 리스트로 제공
     */
    @ExceptionHandler({ MethodArgumentNotValidException.class, BindException.class })
    public ResponseEntity<ErrorResponse> handleValidationException(
            Exception ex,
            HttpServletRequest request
    ) {
        var bindingResult = ex instanceof MethodArgumentNotValidException
                ? ((MethodArgumentNotValidException) ex).getBindingResult()
                : ((BindException) ex).getBindingResult();

        var fieldErrors = bindingResult.getFieldErrors().stream()
                .map(err -> new ErrorResponse.FieldError(
                        err.getField(),
                        err.getRejectedValue(),
                        err.getDefaultMessage()
                ))
                .collect(Collectors.toList());

        var status = HttpStatus.BAD_REQUEST;
        var body = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .code(ErrorCode.INVALID_PAYLOAD.getCode())
                .message(ErrorCode.INVALID_PAYLOAD.getMessage())
                .path(request.getRequestURI())
                .fieldErrors(fieldErrors)
                .build();

        return new ResponseEntity<>(body, status);
    }

    /**
     * 비지니스 로직 에러
     */
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(
            BaseException ex,
            HttpServletRequest request
    ) {
        var status = ex.getStatus();
        var body = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .code(ex.getErrorCode().getCode())
                .message(ex.getErrorCode().getMessage())
                .systemMessage(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(body, status);
    }

    /**
     * 그 외 예상치 못한 에러
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllUncaught(
            Exception ex,
            HttpServletRequest request
    ) {
        var status = HttpStatus.INTERNAL_SERVER_ERROR;
        var body = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .code(ErrorCode.INTERNAL_ERROR.getCode())
                .message(ErrorCode.INTERNAL_ERROR.getMessage())
                .systemMessage(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(body, status);
    }
}