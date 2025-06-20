package fc.innercircle.sanghyukonboarding.formreply.domain.model

import fc.innercircle.sanghyukonboarding.formreply.domain.model.vo.Answers
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class FormReply(
    val id: String = "",
    val formId: String,
    val submittedAt: LocalDateTime,
    answers: List<Answer>
) {
    val answers: Answers

    init {
        this.answers = filteringNewOrFormReplyOfThis(answers)
    }

    private fun filteringNewOrFormReplyOfThis(answers: List<Answer>): Answers {
        return Answers(
            values = answers.filter { it ->
                it.isNew() || it.isFormReplyOf(this)
            }
        )
    }

    fun formattedSubmittedAt(): String {
        val isoLocalDateTime: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
        return submittedAt.format(isoLocalDateTime)
    }
}
