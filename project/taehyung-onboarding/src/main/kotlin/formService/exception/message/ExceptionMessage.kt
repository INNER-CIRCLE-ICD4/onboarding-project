package formService.exception.message

enum class ExceptionMessage(
    val code: CommErrorCode,
    val message: String,
) {
    BAD_REQUEST_DEFAULT(CommErrorCode.BAD_REQUEST_COMM_CODE, "잘못된 요청입니다."),
    INTERNAL_ERROR_MESSAGE(CommErrorCode.INTERNAL_CODE, "Internal Server Error"),
}

/**
 * COMM: 10000 ~ 19999
 * SURVEY: 20000 ~ 29999
 * ANSWER: 30000 ~ 39999
 */

enum class CommErrorCode(
    val code: String,
) {
    BAD_REQUEST_COMM_CODE("10001"),
    INTERNAL_CODE("10002"),
}
