package com.innercircle.survey.response.adapter.`in`.web.dto

import com.innercircle.survey.response.domain.Answer
import com.innercircle.survey.response.domain.Response
import java.time.LocalDateTime

data class ResponseDto(
    val id: String,
    val surveyId: String,
    val surveyVersion: Int,
    val respondentId: String?,
    val answers: List<AnswerDto>,
    val createdAt: LocalDateTime,
) {
    data class AnswerDto(
        val id: String,
        val questionId: String,
        val questionTitle: String,
        val questionType: String,
        val textValue: String?,
        val selectedChoiceIds: Set<String>,
    )
}

fun Response.toDto(): ResponseDto =
    ResponseDto(
        id = id.toString(),
        surveyId = survey.id.toString(),
        surveyVersion = surveyVersion,
        respondentId = respondentId,
        answers = answers.map { it.toDto() },
        createdAt = createdAt,
    )

fun Answer.toDto(): ResponseDto.AnswerDto =
    ResponseDto.AnswerDto(
        id = id.toString(),
        questionId = questionId.toString(),
        questionTitle = questionTitle,
        questionType = questionType.name,
        textValue = textValue,
        selectedChoiceIds = selectedChoiceIds.map { it.toString() }.toSet(),
    )
