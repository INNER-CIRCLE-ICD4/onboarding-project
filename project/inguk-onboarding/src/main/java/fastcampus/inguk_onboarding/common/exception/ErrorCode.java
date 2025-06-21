package fastcampus.inguk_onboarding.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    INVALID_INOUT_VALUE(400, "입력값이 올바르지 않습니다."),
    NOT_FOUND(404, "페이지를 찾을 수 없습니다."),
    INTERNAL_ERROR(500, "서버 내부 오류가 발생했습니다."),

    INVALID_SURVEY_ITEM_COUNT(2000,"설문 항목은 1개 ~ 10개까지 포함할 수 있습니다."),
    OPTIONS_REQUIRED(2001, "선택형 항목은 선택 옵션이 필요합니다."),
    INVALID_CHOICE_OPTION(2002, "선택 옵션이 올바르지 않습니다."),
    DUPLICATE_SURVEY_ITEM(2003, "중복된 항목입니다.");

    private final Integer code;
    private final String message;

    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
