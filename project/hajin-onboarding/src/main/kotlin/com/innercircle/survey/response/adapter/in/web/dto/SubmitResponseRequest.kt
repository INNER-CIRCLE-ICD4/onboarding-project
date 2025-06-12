package com.innercircle.survey.response.adapter.`in`.web.dto

import com.innercircle.survey.response.application.port.`in`.ResponseUseCase
import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import java.util.UUID

data class SubmitResponseRequest(
    val respondentId: String? = null,
    @field:Valid
    @field:NotEmpty(message = "최소 하나 이상의 응답이 필요합니다.")
    val answers: List<AnswerRequest>,
) {
    data class AnswerRequest(
        val questionId: UUID,
        val textValue: String? = null,
        val selectedChoiceIds: Set<UUID>? = null,
    )
}

fun SubmitResponseRequest.toCommand(surveyId: UUID): ResponseUseCase.SubmitResponseCommand =
    ResponseUseCase.SubmitResponseCommand(
        surveyId = surveyId,
        respondentId = respondentId,
        answers = answers.map { it.toCommand() },
    )

fun SubmitResponseRequest.AnswerRequest.toCommand(): ResponseUseCase.SubmitResponseCommand.AnswerCommand =
    ResponseUseCase.SubmitResponseCommand.AnswerCommand(
        questionId = questionId,
        textValue = textValue,
        selectedChoiceIds = selectedChoiceIds,
    )
