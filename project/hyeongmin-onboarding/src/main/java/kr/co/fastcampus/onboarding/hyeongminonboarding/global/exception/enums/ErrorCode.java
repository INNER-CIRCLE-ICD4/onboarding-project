package kr.co.fastcampus.onboarding.hyeongminonboarding.global.exception.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public enum ErrorCode {
    INVALID_PAYLOAD(HttpStatus.BAD_REQUEST, "INVALID_PAYLOAD", "잘못된 요청 데이터입니다."),
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "VALIDATION_FAILED", "입력값 검증에 실패했습니다."),
    SURVEY_QUESTION_OPTION_EMPTY(HttpStatus.BAD_REQUEST, "SURVEY_QUESTION_OPTION_EMPTY", "질문 타입에 따라 옵션은 필수값입니다. 질문 데이터의 옵션리스트를 확인하세요."),
    SURVEY_NOT_FOUND(HttpStatus.NOT_FOUND, "SURVEY_NOT_FOUND", "존재하지 않는 설문입니다."),
    RESPONSE_MISMATCH(HttpStatus.UNPROCESSABLE_ENTITY, "RESPONSE_MISMATCH", "응답이 설문 구조와 일치하지 않습니다."),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "서버 내부 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

}
