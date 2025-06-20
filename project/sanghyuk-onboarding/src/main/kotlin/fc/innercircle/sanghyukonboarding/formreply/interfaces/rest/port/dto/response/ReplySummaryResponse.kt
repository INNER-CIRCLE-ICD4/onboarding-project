package fc.innercircle.sanghyukonboarding.formreply.interfaces.rest.port.dto.response

import fc.innercircle.sanghyukonboarding.form.domain.model.Form
import fc.innercircle.sanghyukonboarding.formreply.domain.model.Answer
import fc.innercircle.sanghyukonboarding.formreply.domain.model.FormReply

data class ReplySummaryResponse(
    val formId: String,
    val title: String,
    val description: String,
    val questions: List<QuestionResponse>,

) {
    data class QuestionResponse(
        val questionId: String,
        val title: String,
        val description: String,
        val allAnswers: List<String>
    )

    companion object {
        fun of(form: Form, formReplies: List<FormReply>): ReplySummaryResponse {
            val allAnswers: List<Answer> = formReplies.flatMap { it -> it.answers.list() }
            return ReplySummaryResponse(
                formId = form.id,
                title = form.title,
                description = form.description,
                questions = form.questions.list().map { question ->
                    val questionAnswers: List<Answer> = allAnswers.filter { answer -> answer.questionId == question.id }
                    QuestionResponse(
                        questionId = question.id,
                        title = question.title,
                        description = question.description,
                        allAnswers = questionAnswers.map { it.values }.flatten()
                    )
                }
            )
        }
    }
}
