package fc.innercircle.sanghyukonboarding.form.interfaces.rest.port.dto.request

import fc.innercircle.sanghyukonboarding.form.domain.service.dto.param.QuestionParam

data class FormRequest(
    val title: String,
    val description: String,
    val questions: List<QuestionRequest> = emptyList(),
) {
    data class QuestionRequest(
        val questionTemplateId: String = "",
        val title: String,
        val description: String = "",
        val type: String,
        val required: Boolean,
        val selectableOptions: List<SelectableOptionRequest> = emptyList(),
    ) {
        data class SelectableOptionRequest(
            val text: String,
        )

        fun toParam(): QuestionParam {
            return QuestionParam(
                questionTemplateId = questionTemplateId,
                title = title,
                description = description,
                type = type,
                required = required,
                selectableOptions = selectableOptions.map { QuestionParam.SelectableOptionParam(it.text) }
            )
        }
    }
}
