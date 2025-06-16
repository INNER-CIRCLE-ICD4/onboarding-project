package com.wlghsp.querybox.domain.response

import jakarta.persistence.CollectionTable
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import support.domain.BaseRootEntity

@Entity
class Response(
    val suerveyId: Long,

    @ElementCollection
    @CollectionTable(name = "answer", joinColumns = [JoinColumn(name = "response_id")])
    val answers: List<Answer>,

    id: Long = 0L
): BaseRootEntity<Response>(id) {

    init {
        require(answers.isNotEmpty()) { "응답은 하나 이상의 답변을 포함해야 합니다." }
        require(answers.distinctBy { it.questionId }.size == answers.size) {
            "같은 문항에 중복 응답할 수 없습니다."
        }
    }

}