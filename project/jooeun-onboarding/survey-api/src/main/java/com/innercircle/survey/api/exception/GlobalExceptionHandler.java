package com.innercircle.survey.api.exception;

import com.innercircle.survey.common.exception.BusinessException;
import com.innercircle.survey.common.exception.ErrorCode;
import com.innercircle.survey.common.util.UlidGenerator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 개선된 글로벌 예외 처리 핸들러
 *
 * 표준화된 에러 응답 형태로 모든 예외를 일관되게 처리합니다.
 *
 * 트랜잭션 처리 참고:
 * - 모든 BusinessException은 RuntimeException 계열이므로 트랜잭션 롤백 발생
 * - 컨트롤러에서 try-catch 없이 자연스럽게 예외 전파
 * - GlobalExceptionHandler는 트랜잭션 경계 밖에서 예외 처리
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 비즈니스 예외 처리
     *
     * 모든 비즈니스 로직 예외를 표준화된 형태로 응답합니다.
     * RuntimeException 계열이므로 트랜잭션 자동 롤백됩니다.
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<StandardErrorResponse> handleBusinessException(
            BusinessException e, HttpServletRequest request) {

        String errorId = UlidGenerator.generate();

        log.warn("비즈니스 예외 발생 [{}] - Code: {}, Message: {}",
                errorId, e.getErrorCode().getCode(), e.getMessage(), e);

        StandardErrorResponse response = StandardErrorResponse.builder()
                .errorId(errorId)
                .errorCode(e.getErrorCode().getCode())
                .message(e.getMessage())
                .details(e.getDetails())
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .method(request.getMethod())
                .build();

        return ResponseEntity.status(e.getErrorCode().getHttpStatusCode()).body(response);
    }

    /**
     * 유효성 검증 실패 처리
     *
     * Bean Validation 어노테이션 검증 실패 시 호출됩니다.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardErrorResponse> handleValidationException(
            MethodArgumentNotValidException e, HttpServletRequest request) {

        String errorId = UlidGenerator.generate();

        log.warn("유효성 검증 실패 [{}]: {}", errorId, e.getMessage());

        List<StandardErrorResponse.ValidationErrorDetail> validationErrors = e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> {
                    FieldError fieldError = (FieldError) error;
                    return StandardErrorResponse.ValidationErrorDetail.builder()
                            .field(fieldError.getField())
                            .rejectedValue(fieldError.getRejectedValue())
                            .message(fieldError.getDefaultMessage())
                            .code(fieldError.getCode())
                            .build();
                })
                .collect(Collectors.toList());

        StandardErrorResponse response = StandardErrorResponse.builder()
                .errorId(errorId)
                .errorCode(ErrorCode.VALIDATION_FAILED.getCode())
                .message("입력 데이터 검증에 실패했습니다.")
                .validationErrors(validationErrors)
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .method(request.getMethod())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * 낙관적 락 충돌 처리
     *
     * JPA 낙관적 락 충돌 시 동시성 에러로 처리합니다.
     */
    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<StandardErrorResponse> handleOptimisticLockingFailure(
            OptimisticLockingFailureException e, HttpServletRequest request) {

        String errorId = UlidGenerator.generate();

        log.warn("낙관적 락 충돌 [{}]: {}", errorId, e.getMessage(), e);

        StandardErrorResponse response = StandardErrorResponse.builder()
                .errorId(errorId)
                .errorCode(ErrorCode.OPTIMISTIC_LOCK_FAILED.getCode())
                .message(ErrorCode.OPTIMISTIC_LOCK_FAILED.getMessage())
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .method(request.getMethod())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    /**
     * 잘못된 JSON 형식 처리
     *
     * 요청 본문이 올바른 JSON 형식이 아닌 경우 처리합니다.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<StandardErrorResponse> handleHttpMessageNotReadable(
            HttpMessageNotReadableException e, HttpServletRequest request) {

        String errorId = UlidGenerator.generate();

        log.warn("잘못된 요청 형식 [{}]: {}", errorId, e.getMessage());

        StandardErrorResponse response = StandardErrorResponse.builder()
                .errorId(errorId)
                .errorCode(ErrorCode.INVALID_REQUEST.getCode())
                .message("요청 형식이 올바르지 않습니다.")
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .method(request.getMethod())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * 지원하지 않는 HTTP 메서드 처리
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<StandardErrorResponse> handleMethodNotSupported(
            HttpRequestMethodNotSupportedException e, HttpServletRequest request) {

        String errorId = UlidGenerator.generate();

        log.warn("지원하지 않는 HTTP 메서드 [{}]: {} {}", errorId, request.getMethod(), request.getRequestURI());

        StandardErrorResponse response = StandardErrorResponse.builder()
                .errorId(errorId)
                .errorCode(ErrorCode.METHOD_NOT_ALLOWED.getCode())
                .message(String.format("지원하지 않는 HTTP 메서드입니다: %s", request.getMethod()))
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .method(request.getMethod())
                .build();

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }

    /**
     * 존재하지 않는 엔드포인트 처리
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<StandardErrorResponse> handleNoHandlerFound(
            NoHandlerFoundException e, HttpServletRequest request) {

        String errorId = UlidGenerator.generate();

        log.warn("존재하지 않는 엔드포인트 [{}]: {} {}", errorId, request.getMethod(), request.getRequestURI());

        StandardErrorResponse response = StandardErrorResponse.builder()
                .errorId(errorId)
                .errorCode(ErrorCode.RESOURCE_NOT_FOUND.getCode())
                .message(String.format("요청한 리소스를 찾을 수 없습니다: %s %s", request.getMethod(), request.getRequestURI()))
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .method(request.getMethod())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * IllegalArgumentException 처리
     *
     * 메서드 파라미터가 잘못된 경우 처리합니다.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<StandardErrorResponse> handleIllegalArgument(
            IllegalArgumentException e, HttpServletRequest request) {

        String errorId = UlidGenerator.generate();

        log.warn("잘못된 인수 [{}]: {}", errorId, e.getMessage(), e);

        StandardErrorResponse response = StandardErrorResponse.builder()
                .errorId(errorId)
                .errorCode(ErrorCode.INVALID_PARAMETER.getCode())
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .method(request.getMethod())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * 보안 예외 처리
     *
     * Spring Security 관련 예외를 처리합니다.
     */
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<StandardErrorResponse> handleSecurityException(
            SecurityException e, HttpServletRequest request) {

        String errorId = UlidGenerator.generate();

        log.warn("보안 예외 [{}]: {}", errorId, e.getMessage(), e);

        StandardErrorResponse response = StandardErrorResponse.builder()
                .errorId(errorId)
                .errorCode(ErrorCode.ACCESS_DENIED.getCode())
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .method(request.getMethod())
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    /**
     * 일반적인 런타임 예외 처리
     *
     * 처리되지 않은 RuntimeException을 포착합니다.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<StandardErrorResponse> handleRuntimeException(
            RuntimeException e, HttpServletRequest request) {

        String errorId = UlidGenerator.generate();

        log.error("런타임 예외 발생 [{}]: {}", errorId, e.getMessage(), e);

        StandardErrorResponse response = StandardErrorResponse.builder()
                .errorId(errorId)
                .errorCode(ErrorCode.INTERNAL_SERVER_ERROR.getCode())
                .message("서버 내부 오류가 발생했습니다.")
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .method(request.getMethod())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * 최종 예외 처리 (모든 예외의 최후 보루)
     *
     * 예상치 못한 모든 예외를 처리합니다.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardErrorResponse> handleGeneralException(
            Exception e, HttpServletRequest request) {

        String errorId = UlidGenerator.generate();

        log.error("예상치 못한 예외 발생 [{}]: {}", errorId, e.getMessage(), e);

        StandardErrorResponse response = StandardErrorResponse.builder()
                .errorId(errorId)
                .errorCode(ErrorCode.INTERNAL_SERVER_ERROR.getCode())
                .message("서버 내부 오류가 발생했습니다.")
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .method(request.getMethod())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
