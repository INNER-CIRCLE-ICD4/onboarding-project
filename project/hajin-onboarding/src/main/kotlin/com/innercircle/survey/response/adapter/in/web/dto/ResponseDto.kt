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
    ) {
        companion object {
            fun from(answer: Answer): AnswerDto =
                AnswerDto(
                    id = answer.id.toString(),
                    questionId = answer.questionId.toString(),
                    questionTitle = answer.questionTitle,
                    questionType = answer.questionType.name,
                    textValue = answer.textValue,
                    selectedChoiceIds = answer.selectedChoiceIds.map { it.toString() }.toSet(),
                )
        }
    }

    companion object {
        fun from(response: Response): ResponseDto =
            ResponseDto(
                id = response.id.toString(),
                surveyId = response.survey.id.toString(),
                surveyVersion = response.surveyVersion,
                respondentId = response.respondentId,
                answers = response.answers.map { AnswerDto.from(it) },
                createdAt = response.createdAt,
            )
    }
}
