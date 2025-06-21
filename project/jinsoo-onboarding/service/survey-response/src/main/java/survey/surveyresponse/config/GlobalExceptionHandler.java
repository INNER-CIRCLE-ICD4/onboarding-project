package survey.surveyresponse.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import survey.common.exception.ApiException;
import survey.surveyresponse.config.response.ErrorResponse;
import survey.surveyresponse.config.response.SurveyErrorResponse;

import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static survey.common.exception.ErrorType.INVALID_PARAMETER;
import static survey.common.exception.ErrorType.NO_RESOURCE;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<SurveyErrorResponse> handleApplicationException(ApplicationException e) {
        log.error("Application Exception occurred. message={}, className={}", e.getErrorType().getDescription(), e.getClass().getName());
        return ResponseEntity.status(e.getHttpStatus()).body(new SurveyErrorResponse(e.getErrorType().getDescription(), e.getErrorType()));
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException e) {
        log.error("Api Exception occurred. message={}, className={}", e.getErrorMessage(), e.getClass().getName());
        return ResponseEntity.status(e.getHttpStatus()).body(new ErrorResponse(e.getMessage(), e.getErrorType()));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(BindException e) {
        log.error("Bind Exception occurred. message={}, className={}", e.getMessage(), e.getClass().getName());
        return ResponseEntity.status(BAD_REQUEST).body(new ErrorResponse(createMessage(e), INVALID_PARAMETER));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException(NoResourceFoundException e) {
        log.error("NoResourceFound Exception occurred. message={}, className={}", e.getMessage(), e.getClass().getName());
        return ResponseEntity.status(BAD_REQUEST)
                .body(new ErrorResponse(NO_RESOURCE.getDescription(), NO_RESOURCE));

    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.error("MissingServletRequestParameter Exception occurred. parameterName={}, message={}",
                e.getParameterName(), e.getMessage());
        return ResponseEntity.status(BAD_REQUEST)
                .body(new ErrorResponse(INVALID_PARAMETER.getDescription(), INVALID_PARAMETER));

    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("MethodArgumentTypeMismatch Exception occurred. parameterName={}, message={}",
                e.getParameter(), e.getMessage());
        return ResponseEntity.status(BAD_REQUEST)
                .body(new ErrorResponse(INVALID_PARAMETER.getDescription(), INVALID_PARAMETER));
    }

    private String createMessage(BindException e) {
        if (e.getFieldError() != null && e.getFieldError().getDefaultMessage() != null) {
            return e.getFieldError().getDefaultMessage();
        }
        return e.getFieldErrors()
                .stream()
                .map(FieldError::getField).collect(Collectors.joining(", ")) + "값들이 정확하지 않습니다.";
    }
}
