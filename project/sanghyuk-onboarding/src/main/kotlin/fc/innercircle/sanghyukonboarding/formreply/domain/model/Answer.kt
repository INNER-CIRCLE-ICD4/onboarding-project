package fc.innercircle.sanghyukonboarding.formreply.domain.model

class Answer(
    val id: String = "",
    val text: String = "",
    val selectableOptionIds: List<String> = emptyList(),
    val questionSnapshotId: String,
    val formReplyId: String = "",
) {

    fun isNew(): Boolean {
        return id.isBlank()
    }

    fun isFormReplyOf(formReply: FormReply): Boolean {
        return formReplyId == formReply.id
    }
}
