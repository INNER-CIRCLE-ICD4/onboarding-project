package com.innercircle.survey.survey.adapter.`in`.web.dto

import com.innercircle.survey.survey.domain.Choice
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
}

fun Survey.toResponse(): SurveyResponse =
    SurveyResponse(
        id = id.toString(),
        title = title,
        description = description,
        version = version,
        questions = questions.map { it.toResponse() },
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

fun Question.toResponse(): SurveyResponse.QuestionResponse =
    SurveyResponse.QuestionResponse(
        id = id.toString(),
        title = title,
        description = description,
        type = type.name,
        required = required,
        choices = choices.map { it.toResponse() },
    )

fun Choice.toResponse(): SurveyResponse.ChoiceResponse =
    SurveyResponse.ChoiceResponse(
        id = id.toString(),
        text = text,
    )
