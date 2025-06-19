
package fc.innercircle.sanghyukonboarding.form.domain.service

import fc.innercircle.sanghyukonboarding.form.domain.model.Form
import fc.innercircle.sanghyukonboarding.form.domain.model.QuestionTemplate
import fc.innercircle.sanghyukonboarding.form.domain.model.SelectableOption
import fc.innercircle.sanghyukonboarding.form.domain.service.dto.param.QuestionParam
import org.springframework.stereotype.Component

@Component
class FormEditor {

    fun newForm(
        title: String,
        description: String,
        params: List<QuestionParam>,
    ): Form {
        val questionTemplates = params.mapIndexed { idx, param ->
            createNewQuestionTemplate(param = param, displayOrder = idx)
        }
        return Form(
            title = title,
            description = description,
            questionTemplates = questionTemplates
        )
    }

    fun edit(
        form: Form,
        title: String,
        description: String,
        params: List<QuestionParam>,
    ): Form {
        val questionTemplates = params.mapIndexed { idx, param ->
            updateOrRenewQuestionTemplate(form, param, idx)
        }

        return form.edited(
            title = title,
            description = description,
            questionTemplates = questionTemplates
        )
    }

    private fun updateOrRenewQuestionTemplate(form: Form, param: QuestionParam, displayOrder: Int): QuestionTemplate {
        return if (form.hasQuestionTemplate(param.questionTemplateId)) {
            val selectableOptions = buildSelectableOptions(param.selectableOptions)
            form.getQuestionTemplate(param.questionTemplateId)
                .edited(
                    title = param.title,
                    description = param.description,
                    type = param.type,
                    required = param.required,
                    displayOrder = displayOrder,
                    selectableOptions = selectableOptions,
                )
        } else {
            createNewQuestionTemplate(form.id, param, displayOrder)
        }
    }

    private fun createNewQuestionTemplate(formId: String = "", param: QuestionParam, displayOrder: Int): QuestionTemplate {
        return QuestionTemplate(
            id = param.questionTemplateId,
            required = param.required,
            displayOrder = displayOrder,
            formId = formId
        ).addSnapshot(
            title = param.title,
            description = param.description,
            type = param.type,
            selectableOptions = buildSelectableOptions(param.selectableOptions)
        )
    }

    private fun buildSelectableOptions(
        options: List<QuestionParam.SelectableOptionParam>
    ): List<SelectableOption> {
        return options.mapIndexed { index, option ->
            SelectableOption(
                text = option.text,
                displayOrder = index
            )
        }
    }
}
