package com.innercircle.survey.response.application.port.`in`

import com.innercircle.survey.response.domain.Response
import java.util.UUID

interface ResponseUseCase {
    fun submitResponse(command: SubmitResponseCommand): Response

    data class SubmitResponseCommand(
        val surveyId: UUID,
        val respondentId: String? = null,
        val answers: List<AnswerCommand>,
    ) {
        data class AnswerCommand(
            val questionId: UUID,
            val textValue: String? = null,
            val selectedChoiceIds: Set<UUID>? = null,
        )
    }
}
