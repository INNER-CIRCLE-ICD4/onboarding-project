package fc.innercircle.sanghyukonboarding.form.domain.service.dto.param

data class QuestionParam(
    val questionTemplateId: String = "",
    val title: String,
    val description: String = "",
    val type: String,
    val required: Boolean,
    val selectableOptions: List<SelectableOptionParam> = emptyList(),
) {
    data class SelectableOptionParam(
        val text: String,
    )
}
