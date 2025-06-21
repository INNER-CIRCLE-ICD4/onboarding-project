package fc.innercircle.sanghyukonboarding.common.interfaces.rest.dto.response

import fc.innercircle.sanghyukonboarding.common.domain.exception.ErrorCode

data class ApiError(val code: String, val message: String) {

    companion object {
        fun from(errorCode: ErrorCode.FormattedErrorCode): ApiError {
            return ApiError(
                code = errorCode.code,
                message = errorCode.message
            )
        }

        fun from(errorCode: ErrorCode): ApiError {
            return ApiError(
                code = errorCode.name,
                message = errorCode.message
            )
        }
    }
}
