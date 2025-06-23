package com.innercircle.survey.response.domain

import com.innercircle.survey.common.domain.BaseEntity
import com.innercircle.survey.response.domain.exception.MissingRequiredAnswerException
import com.innercircle.survey.survey.domain.Survey
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "RESPONSES")
class Response private constructor(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    val survey: Survey,
    @Column(name = "survey_version", nullable = false)
    val surveyVersion: Int,
    // 익명 응답 가능
    @Column(name = "respondent_id", length = 100)
    val respondentId: String? = null,
) : BaseEntity() {
    @OneToMany(
        mappedBy = "response",
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        fetch = FetchType.LAZY,
    )
    private val _answers: MutableList<Answer> = mutableListOf()

    val answers: List<Answer>
        get() = _answers.toList()

    fun addAnswer(answer: Answer) {
        _answers.add(answer)
        answer.response = this
    }

    fun validateCompleteness() {
        val requiredQuestionIds =
            survey.questions
                .filter { it.required }
                .map { it.id }
                .toSet()

        val answeredQuestionIds =
            answers
                .map { it.questionId }
                .toSet()

        val missingRequiredQuestions = requiredQuestionIds - answeredQuestionIds

        if (missingRequiredQuestions.isNotEmpty()) {
            throw MissingRequiredAnswerException(missingRequiredQuestions)
        }
    }

    companion object {
        fun create(
            survey: Survey,
            respondentId: String? = null,
            answers: List<Answer> = emptyList(),
        ): Response {
            val response =
                Response(
                    survey = survey,
                    surveyVersion = survey.version,
                    respondentId = respondentId,
                )

            answers.forEach { response.addAnswer(it) }
            response.validateCompleteness()

            return response
        }
    }
}
