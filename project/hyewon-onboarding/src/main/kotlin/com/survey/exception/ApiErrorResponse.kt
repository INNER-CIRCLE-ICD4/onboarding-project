package com.survey.exception

import java.time.Instant

/**
 * 모든 예외 응답 공통 포맷
 */
data class ApiErrorResponse(
    val error: String,
    val code: Int,
    val timestamp: Instant = Instant.now(),
    val path: String,
    val details: String? = null
)