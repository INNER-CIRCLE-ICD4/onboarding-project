package com.innercircle.survey.common.dto

import org.springframework.data.domain.Page

data class PageResponse<T>(
    val content: List<T>,
    val pageNumber: Int,
    val pageSize: Int,
    val totalElements: Long,
    val totalPages: Int,
    val first: Boolean,
    val last: Boolean,
    val empty: Boolean,
) {
    companion object {
        fun <T> of(page: Page<T>): PageResponse<T> {
            return PageResponse(
                content = page.content,
                pageNumber = page.number,
                pageSize = page.size,
                totalElements = page.totalElements,
                totalPages = page.totalPages,
                first = page.isFirst,
                last = page.isLast,
                empty = page.isEmpty,
            )
        }

        fun <T, R> of(
            page: Page<T>,
            converter: (T) -> R,
        ): PageResponse<R> {
            return PageResponse(
                content = page.content.map(converter),
                pageNumber = page.number,
                pageSize = page.size,
                totalElements = page.totalElements,
                totalPages = page.totalPages,
                first = page.isFirst,
                last = page.isLast,
                empty = page.isEmpty,
            )
        }
    }
}
