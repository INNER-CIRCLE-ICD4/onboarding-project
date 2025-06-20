package fc.innercircle.sanghyukonboarding.formreply.interfaces.rest.port.dto.response

data class FormReplyResponse1(
    val formId: String,
    val title: String,
    val description: String,
    val replies: List<FormReplyResponse>
) {

    data class FormReplyResponse(
        val replyId: String,
        val submittedAt: String,
    ) {

    }
}
