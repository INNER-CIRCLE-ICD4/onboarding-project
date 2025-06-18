package com.wlghsp.querybox.domain.response

import jakarta.persistence.CollectionTable
import jakarta.persistence.ElementCollection
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import lombok.`val`

class Answers(
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "response_answers", joinColumns = [JoinColumn(name = "response_id")])
    val values: List<Answer> = listOf(),
) {
    init {
        require(values.isNotEmpty()) { "응답은 하나 이상의 답변을 포함해야 합니다." }
        require(values.distinctBy { it.questionId }.size == values.size) {
            "같은 문항에 중복 응답할 수 없습니다."
        }
    }

    fun values() : List<Answer> = values.toList()
}