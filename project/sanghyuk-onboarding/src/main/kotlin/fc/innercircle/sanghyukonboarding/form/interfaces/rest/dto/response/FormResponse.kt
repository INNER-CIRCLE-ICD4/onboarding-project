package fc.innercircle.sanghyukonboarding.form.interfaces.rest.dto.response

import fc.innercircle.sanghyukonboarding.form.domain.model.Form
import fc.innercircle.sanghyukonboarding.form.domain.model.QuestionTemplate

data class FormResponse(
    val id: String,
    val title: String,
    val description: String,
    val questions: List<Question>
) {
    data class Question(
        val id: String,
        val title: String,
        val description: String,
        val type: String,
        val required: Boolean,
        val selectable: List<Selectable>,
    ) {
        data class Selectable(
            val optionId: String,
            val value: String,
        )

        companion object {
            fun from(questionTemplate: QuestionTemplate): Question {
                val questionSnapshot = questionTemplate.getLatestSnapshot()
                return Question(
                    id = questionSnapshot.id,
                    title = questionSnapshot.title,
                    description = questionSnapshot.description,
                    type = questionSnapshot.type.name,
                    required = questionTemplate.required,
                    selectable = questionSnapshot.selectableOptions.list().map { option ->
                        Selectable(
                            optionId = option.id,
                            value = option.text
                        )
                    }
                )
            }
        }
    }

    companion object {
        fun from(form: Form): FormResponse {
            return FormResponse(
                id = form.id,
                title = form.title,
                description = form.description,
                questions = form.questionTemplates.list().map { Question.from(it) }
            )
        }
    }
}
