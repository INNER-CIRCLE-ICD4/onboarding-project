package com.innercircle.survey.common.dto

import org.springframework.data.domain.Sort

data class PageRequest(
    val page: Int = 0,
    val size: Int = 20,
    val sort: String? = null,
) {
    init {
        require(page >= 0) { "페이지 번호는 0 이상이어야 합니다." }
        require(size in 1..100) { "페이지 크기는 1 이상 100 이하여야 합니다." }
    }

    fun toPageable(): org.springframework.data.domain.PageRequest {
        return if (sort.isNullOrBlank()) {
            org.springframework.data.domain.PageRequest.of(page, size)
        } else {
            val sortParts = sort.split(",")
            val sortOrder =
                if (sortParts.size > 1 && sortParts[1].equals("desc", ignoreCase = true)) {
                    Sort.Order.desc(sortParts[0])
                } else {
                    Sort.Order.asc(sortParts[0])
                }
            org.springframework.data.domain.PageRequest.of(page, size, Sort.by(sortOrder))
        }
    }
}
