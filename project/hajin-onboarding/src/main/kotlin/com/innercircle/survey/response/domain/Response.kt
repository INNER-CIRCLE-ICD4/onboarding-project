package com.innercircle.survey.response.domain

import com.innercircle.survey.common.domain.BaseEntity
import com.innercircle.survey.survey.domain.Survey
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "RESPONSES")
class Response private constructor(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    val survey: Survey,
    @Column(name = "survey_snapshot", columnDefinition = "TEXT")
    val surveySnapshot: String,
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

    fun getOriginalSurveyTitle(): String {
        // 간단한 파싱 (실제로는 JSON 파서 사용)
        return surveySnapshot.substringAfter("\"title\": \"")
            .substringBefore("\"")
    }

    fun getOriginalSurveyVersion(): Int {
        return surveySnapshot.substringAfter("\"version\": ")
            .substringBefore(",").toInt()
    }

    fun addAnswer(answer: Answer) {
        validateAnswer(answer)
        _answers.add(answer)
        answer.response = this
    }

    fun findAnswerByQuestionId(questionId: UUID): Answer? {
        return _answers.find { it.question.id == questionId }
    }

    fun hasAnsweredAllRequiredQuestions(): Boolean {
        val requiredQuestionIds =
            survey.questions
                .filter { it.required }
                .map { it.id }
                .toSet()

        val answeredQuestionIds =
            _answers
                .map { it.question.id }
                .toSet()

        return requiredQuestionIds.all { it in answeredQuestionIds }
    }

    private fun validateAnswer(answer: Answer) {
        require(_answers.none { it.question.id == answer.question.id }) {
            "이미 해당 질문에 대한 답변이 존재합니다."
        }

        require(survey.questions.any { it.id == answer.question.id }) {
            "답변하려는 질문이 설문조사에 존재하지 않습니다."
        }
    }

    companion object {
        fun create(
            survey: Survey,
            answers: List<Answer> = emptyList(),
        ): Response {
            val snapshot = createSurveySnapshot(survey)

            val response = Response(survey, snapshot)
            answers.forEach { response.addAnswer(it) }

            require(response.hasAnsweredAllRequiredQuestions()) {
                "모든 필수 항목에 답변해야 합니다."
            }

            return response
        }

        private fun createSurveySnapshot(survey: Survey): String {
            val questions =
                survey.questions.joinToString(";") { question ->
                    val choices = question.choices.joinToString(",") { it.text }
                    "${question.id}|${question.title}|${question.type}|${question.required}|$choices"
                }

            return """
                {
                  "surveyId": "${survey.id}",
                  "title": "${survey.title}",
                  "description": "${survey.description}",
                  "version": ${survey.version},
                  "questions": "$questions"
                }
                """.trimIndent()
        }
    }
}
