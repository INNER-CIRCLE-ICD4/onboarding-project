package com.wlghsp.querybox.domain.response

import jakarta.persistence.Embeddable


@Embeddable
class Answer(
    val questionId: Long,
    val text: String? = null,
    val selectedOptionIds: List<Long>? = null,
) {
    init {
        // 텍스트형 응답
        if (selectedOptionIds == null) {
            require(!text.isNullOrBlank()) { "주관식 응답은 비어 있을 수 없습니다." }
        }

        // 선택형 응답
        selectedOptionIds?.let { require(it.isNotEmpty()) { "선택형 응답은 하나 이상 선택해야 합니다." } }
    }

}