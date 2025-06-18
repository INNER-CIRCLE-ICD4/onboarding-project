package fc.innercircle.sanghyukonboarding.form.interfaces.rest.dto.response

import fc.innercircle.sanghyukonboarding.form.domain.model.Form
import fc.innercircle.sanghyukonboarding.form.domain.model.QuestionTemplate

data class FormResponse(
    val formId: String,
    val title: String,
    val description: String,
    val questions: List<Question>
) {
    data class Question(
        val questionTemplateId: String,
        val title: String,
        val description: String,
        val type: String,
        val required: Boolean,
        val version: Long,
        val selectableOptions: List<SelectableOption>,
    ) {
        data class SelectableOption(
            val selectableOptionId: String,
            val text: String,
        )

        companion object {
            fun from(questionTemplate: QuestionTemplate): Question {
                val questionSnapshot = questionTemplate.getLatestSnapshot()
                return Question(
                    questionTemplateId = questionTemplate.id,
                    title = questionSnapshot.title,
                    description = questionSnapshot.description,
                    type = questionSnapshot.type.name,
                    required = questionTemplate.required,
                    version = questionSnapshot.version,
                    selectableOptions = questionSnapshot.selectableOptions.list().map { option ->
                        SelectableOption(
                            selectableOptionId = option.id,
                            text = option.text
                        )
                    }
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
                questions = form.questionTemplates.list().map {
                    Question.from(it)
                }
            )
        }
    }
}
