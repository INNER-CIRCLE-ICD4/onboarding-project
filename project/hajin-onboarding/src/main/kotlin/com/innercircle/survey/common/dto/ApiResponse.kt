package com.innercircle.survey.common.dto

import java.time.LocalDateTime

data class ApiResponse<T>(
    val success: Boolean = true,
    val data: T? = null,
    val message: String? = null,
    val timestamp: LocalDateTime = LocalDateTime.now(),
) {
    companion object {
        fun <T> success(
            data: T? = null,
            message: String? = null,
        ): ApiResponse<T> {
            return ApiResponse(
                success = true,
                data = data,
                message = message,
            )
        }

        fun success(message: String? = null): ApiResponse<Unit> {
            return ApiResponse(
                success = true,
                data = null,
                message = message,
            )
        }
    }
}
