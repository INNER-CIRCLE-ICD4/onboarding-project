package fc.innercircle.sanghyukonboarding.common.domain.exception

import fc.innercircle.sanghyukonboarding.form.domain.model.vo.InputType
import org.springframework.http.HttpStatus
import java.text.MessageFormat

enum class ErrorCode(
    val status: HttpStatus,
    val template: String,
) {
    // 클라이언트의 잘못된 요청에 대한 에러코드 (400)
    // 설문 입력값 유효성 검사 에러코드
    INVALID_FORM_TITLE(HttpStatus.BAD_REQUEST, "설문 제목이 유효하지 않습니다. 제목은 1글자 이상 255자 이하이어야 합니다. (현재 입력값: {0})"),
    INVALID_FORM_DESCRIPTION(HttpStatus.BAD_REQUEST, "설문 설명이 유효하지 않습니다. 설명은 1글자 이상 1000자 이하이어야 합니다. (현재 입력값: {0})"),

    // 질문 입력값 유효성 검사 에러코드
    INVALID_QUESTION_VERSION(HttpStatus.BAD_REQUEST, "질문 템플릿 버전이 유효하지 않습니다. 버전은 0 이상의 정수여야 합니다. (현재 입력값: {0})"),

    // 설문 항목 입력값 유효성 검사 에러코드
    INVALID_QUESTION_TITLE(
        HttpStatus.BAD_REQUEST,
        "질문 제목이 유효하지 않습니다. 제목은 빈 문자열이 아니어야 하며, 최대 500자 이내여야 합니다. (현재 입력값: {0})"
    ),
    INVALID_QUESTION_DESCRIPTION(
        HttpStatus.BAD_REQUEST,
        "질문 설명이 유효하지 않습니다. 설명은 빈 문자열이 아니어야 하며, 최대 1000자 이내여야 합니다. (현재 입력값: {0})"
    ),
    INVALID_QUESTION_ORDER(HttpStatus.BAD_REQUEST, "질문 순서가 유효하지 않습니다. 순서는 0 이상의 정수여야 합니다. (현재 입력값: {0})"),
    INVALID_QUESTION_SNAPSHOT_VERSION(
        HttpStatus.BAD_REQUEST,
        "질문 버전이 유효하지 않습니다. 버전은 0 이상의 정수여야 합니다. (현재 입력값: {0})"
    ),
    INVALID_QUESTION_INPUT_TYPE(
        HttpStatus.BAD_REQUEST,
        "질문 입력 타입이 유효하지 않습니다. 입력 타입은 빈 문자열이 아니어야 하며, 정의된 입력 타입 " + InputType.entries.toString() + " 중 하나여야 합니다. (현재 입력값: {0})"
    ),

    // 질문 입력 옵션 유효성 검사 에러코드
    INVALID_ITEM_OPTION_TEXT(
        HttpStatus.BAD_REQUEST,
        "질문 옵션 텍스트가 유효하지 않습니다. 옵션 텍스트는 빈 문자열이 아니어야 하며, 최대 50자 이내여야 합니다. (현재 입력값: {0})"
    ),
    INVALID_ITEM_OPTION_DISPLAY_ORDER(
        HttpStatus.BAD_REQUEST,
        "질문 옵션 순서가 유효하지 않습니다. 순서는 0 이상의 정수여야 합니다. (현재 입력값: {0})"
    ),

    // 리소스를 찾을 수 없는 에러코드 (404)
    FORM_NOT_FOUND(HttpStatus.NOT_FOUND, "설문을 찾을 수 없습니다. [설문 ID]: {0}"),

    // 비즈니스 로직 에러 코드
    EXCEEDS_MAX_QUESTION_COUNT(HttpStatus.CONFLICT, "설문은 최대 10개의 질문을 포함할 수 있습니다. (현재 입력값: {0})"),

    // 서버 에러 코드 (500)
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에서 알 수 없는 오류가 발생했습니다.")
    ;

    fun withArgs(vararg args: Any?): FormattedErrorCode = FormattedErrorCode(this, args.map { it ?: "" }.toTypedArray())

    val message: String
        get() = template

    class FormattedErrorCode(
        private val errorCode: ErrorCode,
        private val args: Array<Any>,
    ) {
        val statusCode: HttpStatus
            get() = errorCode.status
        val code: String
            get() = errorCode.name
        val message: String
            get() = MessageFormat.format(errorCode.message, *args)
    }
}
