package fc.innercircle.sanghyukonboarding.formreply.domain.model

class Answer(
    val id: String = "",
    val values: List<String> = emptyList(),
    val questionId: String,
    val formReplyId: String = "",
) {

    fun isNew(): Boolean {
        return id.isBlank()
    }

    fun isFormReplyOf(formReply: FormReply): Boolean {
        return formReplyId == formReply.id
    }

    fun isEmpty(): Boolean {
        return values.isEmpty()
    }

    companion object {
        fun empty(): Answer {
            return Answer(
                id = "",
                values = emptyList(),
                questionId = "",
                formReplyId = ""
            )
        }
    }
}
