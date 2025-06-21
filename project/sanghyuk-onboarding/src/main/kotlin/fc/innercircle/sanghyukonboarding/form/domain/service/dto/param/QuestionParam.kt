package fc.innercircle.sanghyukonboarding.form.domain.service.dto.param

data class QuestionParam(
    val questionId: String = "",
    val title: String,
    val description: String = "",
    val type: String,
    val required: Boolean,
    val options: List<String> = emptyList(),
)
