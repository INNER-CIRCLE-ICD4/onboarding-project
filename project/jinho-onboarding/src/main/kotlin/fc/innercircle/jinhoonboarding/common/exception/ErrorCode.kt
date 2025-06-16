package fc.innercircle.jinhoonboarding.common.exception

enum class ErrorCode(
    val code: Int,
    val message: String
) {
    SURVEY_NOT_FOUND( 1001, "설문조사를 찾을 수 없습니다."),

}