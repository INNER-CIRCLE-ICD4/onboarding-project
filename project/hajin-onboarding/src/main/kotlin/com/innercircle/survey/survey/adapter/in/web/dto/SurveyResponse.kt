package com.innercircle.survey.survey.adapter.`in`.web.dto

import com.innercircle.survey.survey.domain.Question
import com.innercircle.survey.survey.domain.Survey
import java.time.LocalDateTime

data class SurveyResponse(
    val id: String,
    val title: String,
    val description: String,
    val version: Int,
    val questions: List<QuestionResponse>,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    data class QuestionResponse(
        val id: String,
        val title: String,
        val description: String,
        val type: String,
        val required: Boolean,
        val choices: List<ChoiceResponse>,
    )

    data class ChoiceResponse(
        val id: String,
        val text: String,
    )

    companion object {
        fun from(survey: Survey): SurveyResponse =
            SurveyResponse(
                id = survey.id.toString(),
                title = survey.title,
                description = survey.description,
                version = survey.version,
                questions = survey.questions.map { fromQuestion(it) },
                createdAt = survey.createdAt,
                updatedAt = survey.updatedAt,
            )

        private fun fromQuestion(question: Question): QuestionResponse =
            QuestionResponse(
                id = question.id.toString(),
                title = question.title,
                description = question.description,
                type = question.type.name,
                required = question.required,
                choices =
                    question.choices.map { choice ->
                        ChoiceResponse(
                            id = choice.id.toString(),
                            text = choice.text,
                        )
                    },
            )
    }
}
