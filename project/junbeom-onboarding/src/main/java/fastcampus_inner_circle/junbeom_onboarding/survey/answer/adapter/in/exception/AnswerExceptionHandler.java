package fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.in.exception;

import fastcampus_inner_circle.junbeom_onboarding.survey.answer.application.service.exception.AnswerValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AnswerExceptionHandler {
    @ExceptionHandler(AnswerValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(AnswerValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("서버 오류가 발생했습니다."));
    }

    public static class ErrorResponse {
        private final String message;
        public ErrorResponse(String message) { this.message = message; }
        public String getMessage() { return message; }
    }
} 