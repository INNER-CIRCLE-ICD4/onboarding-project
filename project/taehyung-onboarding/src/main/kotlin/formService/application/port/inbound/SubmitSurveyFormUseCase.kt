package formService.application.port.inbound

import formService.domain.Answer
import formService.domain.InputType
import formService.domain.QuestionAnswer
import formService.util.getTsid
import java.time.LocalDateTime

interface SubmitSurveyFormUseCase {
    fun submitSurveyForm(command: SubmitSurveyFormCommand): AnswerId

    data class SubmitSurveyFormCommand(
        val surveyFormId: String,
        val userId: String,
        val answers: List<SubmitSurveyFormAnswer>,
    ) {
        fun toDomain() =
            Answer(
                id = getTsid(),
                userId = this.userId,
                submittedAt = LocalDateTime.now(),
                values =
                    this.answers.map {
                        QuestionAnswer(
                            questionId = it.questionId,
                            answerValue = it.answerValue,
                            answerType = it.answerType,
                        )
                    },
            )
    }

    data class SubmitSurveyFormAnswer(
        val questionId: Long,
        val answerValue: String,
        val answerType: InputType,
    )

    data class AnswerId(
        val id: String,
    )
}
