package fc.innercircle.sanghyukonboarding.form.domain.dto.command

data class FormUpdateCommand(
    val title: String,
    val description: String,
    val questions: List<Question> = emptyList(),
) {
    data class Question(
        val questionTemplateId: String,
        val title: String,
        val description: String = "",
        val type: String,
        val version: Long,
        val required: Boolean,
        val selectableOptions: List<SelectableOption> = emptyList(),
    ) {
        data class SelectableOption(
            val selectableOptionId: String,
            val text: String,
        )
    }
}
