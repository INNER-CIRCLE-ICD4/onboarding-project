package survey.survey.config;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorType {
    SURVEY_NOT_FOUND("존재하지 않는 설문입니다.", HttpStatus.NOT_FOUND),
    MINIMUM_QUESTION("최소 1개의 질문이 필요합니다.", HttpStatus.BAD_REQUEST),
    MAXIMUM_QUESTION("최대 10개의 질문이 허용됩니다.", HttpStatus.BAD_REQUEST),
    SURVEY_FORM_NOT_CHANGED("설문 변경 내역이 없습니다.", HttpStatus.BAD_REQUEST),
    QUESTION_INDEX_DUPLICATED("질문 항목이 중복되었습니다", HttpStatus.BAD_REQUEST),
    SURVEY_NOT_CURRENT_FORM("설문 양식이 최신이 아닙니다.", HttpStatus.BAD_REQUEST),
    ;

    ErrorType(String description, HttpStatus badRequest) {
        this.description = description;
        this.status = badRequest;
    }

    private final String description;
    private final HttpStatus status;
}
