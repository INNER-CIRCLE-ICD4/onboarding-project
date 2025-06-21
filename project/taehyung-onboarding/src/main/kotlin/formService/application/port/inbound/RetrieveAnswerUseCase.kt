package formService.application.port.inbound

import java.time.LocalDateTime

interface RetrieveAnswerUseCase {
    fun retrieveAnswer(answerId: String): RetrieveAnswerRead

    data class RetrieveAnswerRead(
        val answerId: String,
        val userId: String,
        val submittedAt: LocalDateTime,
        val values: List<RetrieveQuestionAnswer>,
    )

    data class RetrieveQuestionAnswer(
        val answerValue: String,
    )
}
