package fc.innercircle.sanghyukonboarding.formreply.domain.dto.command

data class FormReplyCommand(
    val questionTemplateId: String,
    val text: String = "",
    val selectableOptionIds: List<String> = emptyList()
)
