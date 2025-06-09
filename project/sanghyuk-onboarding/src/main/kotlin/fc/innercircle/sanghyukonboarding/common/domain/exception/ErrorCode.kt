package fc.innercircle.sanghyukonboarding.common.domain.exception

import org.springframework.http.HttpStatus
import java.text.MessageFormat

enum class ErrorCode(
    val status: HttpStatus,
    private val template: String,
) {
    // 클라이언트의 잘못된 요청에 대한 에러코드 (400)
    // 설문 입력값 유효성 검사 에러코드
    INVALID_SURVEY_TITLE(HttpStatus.BAD_REQUEST, "설문 제목이 유효하지 않습니다. 제목은 1글자 이상 255자 이하이어야 합니다. [현재 입력값]: {0}"),
    INVALID_SURVEY_DESCRIPTION(HttpStatus.BAD_REQUEST, "설문 설명이 유효하지 않습니다. 설명은 1글자 이상 1000자 이하이어야 합니다. [현재 입력값]: {0}"),

    // 설문 항목 입력값 유효성 검사 에러코드
    INVALID_SURVEY_ITEM_QUESTION(
        HttpStatus.BAD_REQUEST,
        "설문 항목 질문이 유효하지 않습니다. 질문은 빈 문자열이 아니어야 하며, 최대 500자 이내여야 합니다. [현재 입력값]: {0}",
    ),
    INVALID_SURVEY_ITEM_DESCRIPTION(
        HttpStatus.BAD_REQUEST,
        "설문 항목 설명이 유효하지 않습니다. 설명은 빈 문자열이 아니어야 하며, 최대 1000자 이내여야 합니다. [현재 입력값]: {0}",
    ),
    INVALID_SURVEY_ITEM_REQUIRED(
        HttpStatus.BAD_REQUEST,
        "설문 항목 필수 여부가 유효하지 않습니다. 필수 여부는 true 또는 false 이어야 합니다. [현재 입력값]: {0}",
    ),
    INVALID_SURVEY_ITEM_ORDER(HttpStatus.BAD_REQUEST, "설문 항목 순서가 유효하지 않습니다. 순서는 0 이상의 정수여야 합니다. [현재 입력값]: {0}"),

    // 설문 항목 입력 옵션 유효성 검사 에러코드
    INVALID_ITEM_OPTION_TEXT(
        HttpStatus.BAD_REQUEST,
        "설문 항목 옵션 텍스트가 유효하지 않습니다. 옵션 텍스트는 빈 문자열이 아니어야 하며, 최대 50자 이내여야 합니다. [현재 입력값]: {0}",
    ),
    INVALID_ITEM_OPTION_DISPLAY_ORDER(
        HttpStatus.BAD_REQUEST,
        "설문 항목 옵션 순서가 유효하지 않습니다. 순서는 0 이상의 정수여야 합니다. [현재 입력값]: {0}",
    ),

    // 설문 항목 답변 유효성 검사 에러코드
    INVALID_SURVEY_ITEM_ANSWER_QUESTION(
        HttpStatus.BAD_REQUEST,
        "설문 항목 답변 질문이 유효하지 않습니다. 질문은 빈 문자열이 아니어야 하며, 최대 500자 이내여야 합니다. [현재 입력값]: {0}",
    ),

    INVALID_SURVEY_ITEM_ANSWER_DESCRIPTION(
        HttpStatus.BAD_REQUEST,
        "설문 항목 답변 설명이 유효하지 않습니다. 설명은 빈 문자열이 아니어야 하며, 최대 1000자 이내여야 합니다. [현재 입력값]: {0}",
    ),

    INVALID_SURVEY_ITEM_ANSWER_SURVEY_INPUT_TYPE(
        HttpStatus.BAD_REQUEST,
        "설문 항목 답변 입력 타입이 유효하지 않습니다. 입력 타입은 빈 문자열이 아니어야 하며, 정의된 입력 타입 중 하나여야 합니다. [현재 입력값]: {0}",
    ),
    ;

    fun withArgs(vararg args: Any?): FormattedErrorCode = FormattedErrorCode(this, args.map { it ?: "" }.toTypedArray())

    val message: String
        get() = template

    class FormattedErrorCode(
        private val errorCode: ErrorCode,
        private val args: Array<Any>,
    ) {
        val status: HttpStatus get() = errorCode.status
        val statusCode: String get() = errorCode.status.value().toString()

        val message: String
            get() = MessageFormat.format(errorCode.message, *args)
    }
}
