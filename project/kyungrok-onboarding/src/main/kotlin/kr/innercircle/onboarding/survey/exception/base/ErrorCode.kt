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
    INTERNAL_SERVER_ERROR("ERR001", "internal server error caused."),
    FORBIDDEN("ERR002", "forbidden"),
    UNAUTHORIZED("ERR003", "unauthorized"),
    INVALID_HTTP_REQUEST("ERR004", "Unable to read or parse HTTP request body"),
    METHOD_ARGUMENT_NOT_VALID("ERR005", "Invalid HTTP request body"),
    METHOD_ARGUMENT_TYPE_MISMATCH("ERR006", "Invalid HTTP request value's type"),

    TEST("TEST001", "ee"),
    TES2("TEST002", "ee")
}