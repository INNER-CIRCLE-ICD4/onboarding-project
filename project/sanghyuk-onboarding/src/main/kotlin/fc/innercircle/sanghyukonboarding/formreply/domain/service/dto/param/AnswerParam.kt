package fc.innercircle.sanghyukonboarding.formreply.domain.service.dto.param

data class AnswerParam(
    val questionTemplateId: String,
    val text: String = "",
    val selectableOptionIds: List<String> = emptyList()
)
