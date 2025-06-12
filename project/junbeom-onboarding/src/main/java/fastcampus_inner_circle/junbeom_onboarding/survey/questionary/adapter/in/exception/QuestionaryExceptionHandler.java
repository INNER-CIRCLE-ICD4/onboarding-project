package fastcampus_inner_circle.junbeom_onboarding.survey.questionary.adapter.in.exception;

import fastcampus_inner_circle.junbeom_onboarding.survey.common.exception.AbstractGlobalExceptionHandler;
import fastcampus_inner_circle.junbeom_onboarding.survey.common.exception.ErrorResponse;
import fastcampus_inner_circle.junbeom_onboarding.survey.questionary.application.service.exception.ContentValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice(basePackages = "fastcampus_inner_circle.junbeom_onboarding.survey.questionary")
public class QuestionaryExceptionHandler extends AbstractGlobalExceptionHandler {

    @Override
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        return super.handleGenericException(ex);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        String message = errors.getOrDefault("contents", "유효성 검사 실패") + ": " + (errors.get("contents") != null ? errors.get("contents") : "");
        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Error",
                message,
                LocalDateTime.now().toString()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ContentValidationException.class)
    public ResponseEntity<ErrorResponse> handleContentValidationException(ContentValidationException ex) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Error",
                ex.getMessage(),
                LocalDateTime.now().toString()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
