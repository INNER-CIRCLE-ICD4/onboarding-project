package com.okdori.surveyservice.exception

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.http.HttpStatus.NOT_FOUND

/**
 * packageName    : com.okdori.surveyservice.exception
 * fileName       : ErrorCode
 * author         : okdori
 * date           : 2025. 6. 14.
 * description    :
 */

enum class ErrorCode(
    val status: HttpStatus,
    val message: String,
) {
    INVALID_REQUEST_FORMAT(BAD_REQUEST, "잘못된 요청 데이터입니다."),
    VALIDATION_FAILED(BAD_REQUEST, "유효성 검사에 실패했습니다."),
    SURVEY_NAME_REQUIRED(BAD_REQUEST, "설문조사 이름은 필수입니다."),
    SURVEY_NAME_TOO_LONG(BAD_REQUEST, "설문조사 이름은 200자를 초과할 수 없습니다."),
    SURVEY_DESCRIPTION_TOO_LONG(BAD_REQUEST, "설문조사 설명은 2000자를 초과할 수 없습니다."),
    SURVEY_ITEMS_REQUIRED(BAD_REQUEST, "설문 항목은 최소 1개 이상 필요합니다."),
    SURVEY_ITEMS_LIMIT_EXCEEDED(BAD_REQUEST, "설문 항목은 최대 10개까지 가능합니다."),
    ITEM_NAME_REQUIRED(BAD_REQUEST, "설문 항목 이름은 필수입니다."),
    ITEM_NAME_TOO_LONG(BAD_REQUEST, "설문 항목 이름은 200자를 초과할 수 없습니다."),
    ITEM_DESCRIPTION_TOO_LONG(BAD_REQUEST, "설문 항목 설명은 2000자를 초과할 수 없습니다."),
    SELECT_OPTIONS_REQUIRED(BAD_REQUEST, "선택형 문항에는 선택지가 필요합니다."),
    INVALID_ITEM_TYPE(BAD_REQUEST, "유효하지 않은 설문 항목 타입입니다."),
    REQUIRED_RESPONSE_MISSING(BAD_REQUEST, "필수 항목이 누락되었습니다."),
    INVALID_RESPONSE_FORMAT(BAD_REQUEST, "응답 형식이 올바르지 않습니다."),
    SHORT_TEXT_TOO_LONG(BAD_REQUEST, "단답형 응답은 200자를 초과할 수 없습니다."),
    LONG_TEXT_TOO_LONG(BAD_REQUEST, "장문형 응답은 2000자를 초과할 수 없습니다."),
    INVALID_SINGLE_SELECT(BAD_REQUEST, "단일 선택 항목에는 하나의 값만 선택할 수 있습니다."),
    INVALID_MULTIPLE_SELECT(BAD_REQUEST, "다중 선택 항목의 값이 유효하지 않습니다."),
    MULTIPLE_SELECT_REQUIRED(BAD_REQUEST, "다중 선택 항목에는 최소 하나의 값이 필요합니다."),

    SURVEY_CLOSED(FORBIDDEN, "마감된 설문조사입니다."),

    NOT_FOUND_SURVEY(NOT_FOUND, "설문조사를 찾을 수 없습니다,"),
    NOT_FOUND_SURVEY_ITEM(NOT_FOUND, "설문 항목을 찾을 수 없습니다."),
    NOT_FOUND_RESPONSE(NOT_FOUND, "응답을 찾을 수 없습니다.")
}
