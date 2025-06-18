package survey.surveyresponse.config;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApplicationException extends RuntimeException {
    private final String errorMessage;
    private final ErrorType errorType;
    private final HttpStatus httpStatus;

    public ApplicationException(String errorMessage, ErrorType errorType, HttpStatus httpStatus) {
        this.errorMessage = errorMessage;
        this.errorType = errorType;
        this.httpStatus = httpStatus;
    }

    public ApplicationException(ErrorType errorType) {
        this.errorMessage = errorType.getDescription();
        this.errorType = errorType;
        this.httpStatus = errorType.getStatus();
    }
}
