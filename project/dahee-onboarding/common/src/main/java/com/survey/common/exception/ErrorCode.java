package com.survey.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    // 4xx
    NOT_FOUND(HttpStatus.NOT_FOUND, "데이터가 존재하지 않습니다."),
    VALIDATION_FAIL(HttpStatus.BAD_REQUEST, "입력값 오류입니다."),
    INVALID_ITEM_COUNT(HttpStatus.BAD_REQUEST, "문항은 최소 1개, 최대 10개까지 등록할 수 있습니다."),
    INVALID_ITEM_ID(HttpStatus.BAD_REQUEST, "유효하지 않은 문항 ID입니다: {0}"),
    SURVEY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 설문입니다: {0}"),
    SURVEY_CLOSED(HttpStatus.CONFLICT, "현재 설문 응답 가능 기간이 아닙니다."),
    DUPLICATE_RESPONSE(HttpStatus.CONFLICT, "이미 응답하셨습니다."),
    // 5xx
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String messageTemplate;

    ErrorCode(HttpStatus status, String messageTemplate) {
        this.status = status;
        this.messageTemplate = messageTemplate;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessageTemplate() {
        return messageTemplate;
    }

    public String format(Object... args) {
        return String.format(messageTemplate.replace("{0}", "%s"), args);
    }
}
