package com.okdori.surveyservice.dto

/**
 * packageName    : com.okdori.surveyservice.dto
 * fileName       : PagedResponse
 * author         : okdori
 * date           : 2025. 6. 21.
 * description    :
 */
data class PagedResponse<T>(
    val content: List<T>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Long,
    val last: Boolean
) {
    companion object {
        fun <T> empty(): PagedResponse<T> = PagedResponse(
            content = emptyList(),
            page = 0,
            size = 0,
            totalElements = 0,
            totalPages = 0,
            last = true
        )
    }
}
