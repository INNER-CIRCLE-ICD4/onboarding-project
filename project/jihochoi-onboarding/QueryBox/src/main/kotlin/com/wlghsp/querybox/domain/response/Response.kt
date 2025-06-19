package com.wlghsp.querybox.domain.response

import com.wlghsp.querybox.ui.dto.ResponseDto
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import support.domain.BaseRootEntity

@Entity
class Response(
    val surveyId: Long,

    @Embedded
    val answers: Answers? = Answers(),

    @Column(columnDefinition = "TEXT", nullable = false)
    val snapshot: String,

    id: Long = 0L
): BaseRootEntity<Response>(id) {

    fun toFilteredDtos(questionKeyword: String?, answerKeyword: String?): List<ResponseDto> {
        val filtered = getFilteredAnswers(questionKeyword, answerKeyword)
        return filtered.map { ResponseDto.from(this, it) }
    }

    private fun getFilteredAnswers(
        questionKeyword: String?,
        answerKeyword: String?
    ): List<Answer> = this.answers?.filterAnswer(questionKeyword, answerKeyword) ?: emptyList()

    companion object {
        fun of(surveyId: Long, answers: Answers, snapshot: String): Response {
            return Response(surveyId, answers, snapshot)
        }
    }
}