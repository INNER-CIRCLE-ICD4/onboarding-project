package com.innercircle.survey.common.dto

import com.innercircle.survey.common.exception.ErrorCode
import java.time.LocalDateTime

data class ErrorResponse(
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val errorCode: String,
    val message: String,
    val details: List<FieldError>? = null,
    val path: String? = null,
) {
    data class FieldError(
        val field: String,
        val value: Any?,
        val reason: String,
    )

    companion object {
        fun of(
            errorCode: ErrorCode,
            message: String = errorCode.message,
            path: String? = null,
        ): ErrorResponse {
            return ErrorResponse(
                errorCode = errorCode.code,
                message = message,
                path = path,
            )
        }

        fun of(
            errorCode: ErrorCode,
            fieldErrors: List<FieldError>,
            path: String? = null,
        ): ErrorResponse {
            return ErrorResponse(
                errorCode = errorCode.code,
                message = errorCode.message,
                details = fieldErrors,
                path = path,
            )
        }
    }
}
