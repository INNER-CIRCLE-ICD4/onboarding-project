package fc.innercircle.sanghyukonboarding.form.domain.dto.command

data class FormCommand(
    val title: String,
    val description: String,
    val questions: List<Question> = emptyList(),
) {
    data class Question(
        val questionTemplateId: String = "",
        val title: String,
        val description: String = "",
        val type: String,
        val required: Boolean,
        val selectableOptions: List<SelectableOption> = emptyList(),
    ) {
        data class SelectableOption(
            val text: String,
        )
    }
}
