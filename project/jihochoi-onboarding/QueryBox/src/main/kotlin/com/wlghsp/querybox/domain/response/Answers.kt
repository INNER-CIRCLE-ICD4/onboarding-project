package com.wlghsp.querybox.domain.response

import jakarta.persistence.CollectionTable
import jakarta.persistence.ElementCollection
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn

class Answers(
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "response_answers", joinColumns = [JoinColumn(name = "response_id")])
    val answers: List<Answer> = listOf(),
) {
    fun validate() {
        require(isNotEmpty()) { "응답은 하나 이상의 답변을 포함해야 합니다." }
        require(hasNoDuplicateAnswers()) { "같은 문항에 중복 응답할 수 없습니다." }
    }

    fun values() : List<Answer> = answers.toList()

    fun filterAnswer(questionKeyword: String?, answerKeyword: String?) : List<Answer> {
        return answers.filter { it.matchQuestion(questionKeyword) && it.matchesAnswer(answerKeyword)}
    }

    private fun hasNoDuplicateAnswers(): Boolean = answers.distinctBy { it.questionId }.size == answers.size

    private fun isNotEmpty(): Boolean = answers.isNotEmpty()

    companion object {
        fun of(answers: List<Answer>): Answers {
            val wrappedAnswers = Answers(answers.toMutableList())
            wrappedAnswers.validate()
            return wrappedAnswers
        }
    }
}