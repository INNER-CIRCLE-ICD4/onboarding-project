package com.innercircle.survey.common.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val status: HttpStatus,
    val code: String,
    val message: String,
) {
    // 공통 에러 (COMMON_XXX)
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "COMMON_001", "유효하지 않은 입력값입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "COMMON_002", "지원하지 않는 HTTP 메소드입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_003", "서버 내부 오류가 발생했습니다."),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "COMMON_004", "유효하지 않은 타입입니다."),
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON_005", "요청한 리소스를 찾을 수 없습니다."),

    // 설문조사 관련 에러 (SURVEY_XXX)
    SURVEY_NOT_FOUND(HttpStatus.NOT_FOUND, "SURVEY_001", "설문조사를 찾을 수 없습니다."),
    SURVEY_ITEM_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "SURVEY_002", "설문조사 항목 수가 제한을 초과했습니다."),
    SURVEY_CHOICE_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "SURVEY_003", "선택지 수가 제한을 초과했습니다."),
    SURVEY_INVALID_ITEM_TYPE(HttpStatus.BAD_REQUEST, "SURVEY_004", "유효하지 않은 항목 타입입니다."),
    SURVEY_MISSING_CHOICES(HttpStatus.BAD_REQUEST, "SURVEY_005", "선택형 항목에는 선택지가 필요합니다."),
    SURVEY_DUPLICATE_CHOICE(HttpStatus.BAD_REQUEST, "SURVEY_006", "중복된 선택지가 있습니다."),

    // 응답 관련 에러 (RESPONSE_XXX)
    RESPONSE_NOT_FOUND(HttpStatus.NOT_FOUND, "RESPONSE_001", "응답을 찾을 수 없습니다."),
    RESPONSE_INVALID_ANSWER(HttpStatus.BAD_REQUEST, "RESPONSE_002", "유효하지 않은 응답입니다."),
    RESPONSE_MISSING_REQUIRED(HttpStatus.BAD_REQUEST, "RESPONSE_003", "필수 항목에 대한 응답이 누락되었습니다."),
    RESPONSE_TEXT_TOO_LONG(HttpStatus.BAD_REQUEST, "RESPONSE_004", "텍스트 응답이 너무 깁니다."),
    RESPONSE_INVALID_CHOICE(HttpStatus.BAD_REQUEST, "RESPONSE_005", "유효하지 않은 선택지입니다."),
    RESPONSE_MULTIPLE_CHOICE_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "RESPONSE_006", "단일 선택 항목에 여러 개의 선택지를 선택할 수 없습니다."),
    RESPONSE_SURVEY_MISMATCH(HttpStatus.BAD_REQUEST, "RESPONSE_007", "응답이 설문조사 구조와 일치하지 않습니다."),
}
