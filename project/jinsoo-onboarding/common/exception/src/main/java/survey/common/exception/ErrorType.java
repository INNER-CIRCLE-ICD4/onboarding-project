package survey.common.exception;

import lombok.Getter;

@Getter
public enum ErrorType {
    UNKNOWN("알 수 없는 에러입니다."),
    INVALID_PARAMETER("잘못된 요청값입니다"),
    NO_RESOURCE(" 존재하지 않는 리소스입니다."),
    RESOURCE_VERSION_MISMATCH("리소스 버전이 일치하지 않습니다."),
    ;

    ErrorType(String description) {
        this.description = description;
    }

    private final String description;
}
