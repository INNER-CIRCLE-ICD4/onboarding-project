package kr.innercircle.onboarding.survey.dto.response

import com.fasterxml.jackson.annotation.JsonInclude
import kr.innercircle.onboarding.survey.exception.base.ErrorCode

/**
  * packageName : kr.innercircle.onboarding.survey.dto
  * fileName    : ApiResponse
  * author      : ckr
  * date        : 25. 6. 12.
  * description :
  */

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ApiResponse(
    val success: Boolean = true,
    var data: Any? = null,
    var message: String? = null,
    var error: ApiErrorResponse? = null
) {
    constructor(errorCode: ErrorCode) : this(
        success = false,
        error = ApiErrorResponse(errorCode),
    )

    constructor(apiErrorResponse: ApiErrorResponse) : this(
        success = false,
        error = apiErrorResponse,
    )
}

data class ApiErrorResponse(
    val code: String,
    val message: String,
) {
    constructor(errorCode: ErrorCode) : this(
        code = errorCode.code,
        message = errorCode.message,
    )
}