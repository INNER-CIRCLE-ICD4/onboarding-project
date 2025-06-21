package survey.surveyresponse.config;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorType {
    SURVEY_NOT_CURRENT_FORM("설문 양식이 최신이 아닙니다.", HttpStatus.BAD_REQUEST),
    ;

    ErrorType(String description, HttpStatus badRequest) {
        this.description = description;
        this.status = badRequest;
    }

    private final String description;
    private final HttpStatus status;
}
