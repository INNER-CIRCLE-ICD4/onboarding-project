
package fc.innercircle.sanghyukonboarding.form.domain.service

import fc.innercircle.sanghyukonboarding.form.domain.model.Form
import fc.innercircle.sanghyukonboarding.form.domain.model.Question
import fc.innercircle.sanghyukonboarding.form.domain.model.vo.InputType
import fc.innercircle.sanghyukonboarding.form.domain.service.dto.param.QuestionParam
import org.springframework.stereotype.Component

@Component
class FormEditor {

    fun newForm(
        title: String,
        description: String,
        params: List<QuestionParam>,
    ): Form {
        val questions = params.mapIndexed { idx, param ->
            createNewQuestion(param = param, displayOrder = idx)
        }
        return Form(
            title = title,
            description = description,
            questions = questions
        )
    }

    fun edit(
        form: Form,
        title: String,
        description: String,
        params: List<QuestionParam>,
    ): Form {
        val newQuestions = params.mapIndexed { idx, param ->
            reviseQuestionOrCreate(form, param, idx)
        }
        return form.edited(
            title = title,
            description = description,
            newQuestions = newQuestions
        )
    }

    private fun reviseQuestionOrCreate(form: Form, param: QuestionParam, displayOrder: Int): Question {
        return if (form.hasQuestionTemplate(param.questionId)) {
            form.getQuestionTemplate(param.questionId)
                .revise(
                    required = param.required,
                    displayOrder = displayOrder,
                    type = param.type,
                    title = param.title,
                    description = param.description,
                    options = param.options
                )
        } else {
            createNewQuestion(form.id, param, displayOrder)
        }
    }

    private fun createNewQuestion(formId: String = "", param: QuestionParam, displayOrder: Int): Question {
        return Question(
            id = param.questionId,
            required = param.required,
            displayOrder = displayOrder,
            type = InputType.valueOrThrows(param.type),
            title = param.title,
            description = param.description,
            options = param.options,
            formId = formId
        )
    }
}
