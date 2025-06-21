package fc.innercircle.sanghyukonboarding.form.interfaces.rest.port.dto.request

import fc.innercircle.sanghyukonboarding.form.domain.service.dto.param.QuestionParam

data class FormRequest(
    val title: String,
    val description: String,
    val questions: List<QuestionRequest> = emptyList(),
) {
    data class QuestionRequest(
        val questionId: String = "",
        val title: String,
        val description: String = "",
        val type: String,
        val required: Boolean,
        val options: List<String> = emptyList(),
    ) {
        fun toParam(): QuestionParam {
            return QuestionParam(
                questionId = questionId,
                title = title,
                description = description,
                type = type,
                required = required,
                options = options
            )
        }
    }
}
