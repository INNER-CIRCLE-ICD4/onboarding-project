package kr.innercircle.onboarding.survey.exception.base

/**
 * packageName : kr.innercircle.onboarding.survey.exception
 * fileName    : ErrorCode
 * author      : ckr
 * date        : 25. 6. 12.
 * description :
 */
enum class ErrorCode(
    val code: String,
    val message: String
) {
    INTERNAL_SERVER_ERROR("ERR001", "서버 오류가 발생하였습니다. 관리자에게 문의해주세요."),
    INVALID_HTTP_REQUEST("ERR002", "HTTP 요청 메세지를 읽을 수 없습니다."),
    METHOD_ARGUMENT_NOT_VALID("ERR003", "요청한 값이 검증에 실패했습니다."),
    METHOD_ARGUMENT_TYPE_MISMATCH("ERR004", "요청 URL 혹은 요청 값의 타입이 올바르지 않습니다."),
    INVALID_SURVEY_ITEM_INPUT_TYPE("ERR005", "유효하지 않은 설문조사 항목 입력 종류입니다. 옵션을 추가하려면 SINGLE_CHOICE 혹은 MULTIPLE_CHOICE 여야 합니다."),
    INSUFFICIENT_SURVEY_ITEM_OPTIONS("ERR006", "설문조사 항목 선택 옵션은 최소 2개 이상이어야 합니다."),
    SURVEY_NOT_FOUND("ERR007", "해당 설문조사는 존재하지 않습니다."),
    SURVEY_ITEM_NOT_FOUND("ERR008", "해당 설문조사 항목은 존재하지 않습니다."),
    INVALID_SURVEY_ITEM_OPTION_ANSWER("ERR009", "설문조사 항목에 대한 유효한 응답이 아닙니다. 선택 가능한 옵션중에서 다시 응답해주세요."),
}