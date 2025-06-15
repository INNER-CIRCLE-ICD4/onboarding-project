package fc.innercircle.jinhoonboarding.common.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val status: Int,
    val code: String,
    val message: String
) {

    SURVEY_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "SURVEY_001", "설문조사를 찾을 수 없습니다."),

}