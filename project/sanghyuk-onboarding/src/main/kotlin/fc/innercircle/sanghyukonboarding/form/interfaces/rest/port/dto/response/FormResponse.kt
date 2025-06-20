package fc.innercircle.sanghyukonboarding.form.interfaces.rest.port.dto.response

import fc.innercircle.sanghyukonboarding.form.domain.model.Form
import fc.innercircle.sanghyukonboarding.form.domain.model.Question

data class FormResponse(
    val formId: String,
    val title: String,
    val description: String,
    val questions: List<QuestionResponse>
) {
    data class QuestionResponse(
        val questionId: String,
        val title: String,
        val description: String,
        val type: String,
        val required: Boolean,
        val version: Long,
        val options: List<String>
    ) {

        companion object {
            fun from(question: Question): QuestionResponse {
                return QuestionResponse(
                    questionId = question.id,
                    title = question.title,
                    description = question.description,
                    type = question.type.name,
                    required = question.required,
                    version = question.version,
                    options =  question.options
                )
            }
        }
    }

    companion object {
        fun from(form: Form): FormResponse {
            return FormResponse(
                formId = form.id,
                title = form.title,
                description = form.description,
                questions = form.questions.list().map {
                    QuestionResponse.from(it)
                }
            )
        }
    }
}
