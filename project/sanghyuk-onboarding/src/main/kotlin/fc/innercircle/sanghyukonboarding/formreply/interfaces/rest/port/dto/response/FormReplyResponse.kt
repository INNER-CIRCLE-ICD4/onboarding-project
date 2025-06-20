package fc.innercircle.sanghyukonboarding.formreply.interfaces.rest.port.dto.response

data class FormReplyResponse(
    val formReplyId: String,
    val formId: String,
    val title: String,
    val description: String,
    val questions: List<QuestionResponse>,
    val submittedAt: String,
) {
    data class QuestionResponse(
        val questionTemplateId: String,
        val title: String,
        val description: String,
        val type: String,
        val required: Boolean,
        val version: Long,
        val options: List<String>,
        val answer: List<String>,
    )

}
