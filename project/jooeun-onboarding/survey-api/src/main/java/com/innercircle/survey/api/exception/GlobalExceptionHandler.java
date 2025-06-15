package com.innercircle.survey.api.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 글로벌 예외 처리 핸들러
 * 
 * 애플리케이션 전체에서 발생하는 예외를 일관된 형태로 처리합니다.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 설문조사를 찾을 수 없을 때
     */
    @ExceptionHandler(SurveyNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleSurveyNotFound(SurveyNotFoundException e) {
        log.warn("설문조사 조회 실패: {}", e.getMessage());
        
        Map<String, Object> response = new HashMap<>();
        response.put("error", "SURVEY_NOT_FOUND");
        response.put("message", e.getMessage());
        response.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * 접근 권한이 없을 때
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDenied(AccessDeniedException e) {
        log.warn("접근 권한 없음: {}", e.getMessage());
        
        Map<String, Object> response = new HashMap<>();
        response.put("error", "ACCESS_DENIED");
        response.put("message", e.getMessage());
        response.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    /**
     * 낙관적 락 충돌
     */
    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<Map<String, Object>> handleOptimisticLockingFailure(OptimisticLockingFailureException e) {
        log.warn("낙관적 락 충돌: {}", e.getMessage());
        
        Map<String, Object> response = new HashMap<>();
        response.put("error", "OPTIMISTIC_LOCK_EXCEPTION");
        response.put("message", "다른 사용자가 동시에 수정했습니다. 최신 버전을 다시 조회하여 수정해주세요.");
        response.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    /**
     * 유효성 검증 실패
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException e) {
        log.warn("유효성 검증 실패: {}", e.getMessage());
        
        Map<String, Object> response = new HashMap<>();
        Map<String, String> errors = new HashMap<>();
        
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        response.put("error", "INVALID_REQUEST");
        response.put("message", "입력 데이터가 유효하지 않습니다.");
        response.put("details", errors);
        response.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * 일반적인 IllegalArgumentException
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException e) {
        log.warn("잘못된 인수: {}", e.getMessage());
        
        Map<String, Object> response = new HashMap<>();
        response.put("error", "INVALID_REQUEST");
        response.put("message", e.getMessage());
        response.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * 보안 예외 (SecurityException)
     */
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<Map<String, Object>> handleSecurityException(SecurityException e) {
        log.warn("보안 예외: {}", e.getMessage());
        
        Map<String, Object> response = new HashMap<>();
        response.put("error", "ACCESS_DENIED");
        response.put("message", e.getMessage());
        response.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    /**
     * 기타 모든 예외
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception e) {
        log.error("서버 내부 오류: {}", e.getMessage(), e);
        
        Map<String, Object> response = new HashMap<>();
        response.put("error", "INTERNAL_SERVER_ERROR");
        response.put("message", "서버 내부 오류가 발생했습니다.");
        response.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
