package fc.innercircle.sanghyukonboarding.formreply.interfaces.rest.dto.response

import fc.innercircle.sanghyukonboarding.formreply.domain.model.FormReply

data class FormReplyResponse(
    val formReplyId: String,
    val formId: String,
    val submittedAt: String,
    val answers: List<Answer>
) {
    data class Answer(
        val questionTemplateId: String,
        val answer: String = "",
        val selectableOptionIds: List<String> = emptyList()
    ) {
        companion object {
            fun from(answer: fc.innercircle.sanghyukonboarding.formreply.domain.model.Answer): Answer {
                return Answer(
                    questionTemplateId = answer.questionSnapshotId,
                    answer = answer.answer,
                    selectableOptionIds = answer.selectableOptionIds
                )
            }
        }
    }

    companion object {
        fun from(formReply: FormReply): FormReplyResponse {
            return FormReplyResponse(
                formReplyId = formReply.id,
                formId = formReply.formId,
                submittedAt = formReply.formattedSubmittedAt(),
                answers = formReply.answers.list().map { answer ->
                    Answer.from(answer)
                }
            )
        }
    }
}
