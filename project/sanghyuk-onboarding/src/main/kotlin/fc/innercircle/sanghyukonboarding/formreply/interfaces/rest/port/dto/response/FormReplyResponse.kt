package fc.innercircle.sanghyukonboarding.formreply.interfaces.rest.port.dto.response

import fc.innercircle.sanghyukonboarding.form.domain.model.Form
import fc.innercircle.sanghyukonboarding.form.domain.model.Question
import fc.innercircle.sanghyukonboarding.formreply.domain.model.Answer
import fc.innercircle.sanghyukonboarding.formreply.domain.model.FormReply

/**
 * 설문 답변에 대한 전체 응답 DTO입니다.
 * 특정 설문에 대한 하나의 답변 내용을 담고 있습니다.
 */
data class FormReplyResponse(
    val formReplyId: String,
    val formId: String,
    val title: String,
    val description: String,
    val questions: List<QuestionResponse>,
    val submittedAt: String,
) {
    /**
     * 설문의 개별 질문과 그에 대한 답변을 나타내는 DTO입니다.
     */
    data class QuestionResponse(
        val questionId: String,
        val title: String,
        val description: String,
        val type: String,
        val required: Boolean,
        val version: Long,
        val options: List<String>,
        val answers: List<String>,
    ) {
        companion object {
            /**
             * Question과 Answer 도메인 객체로부터 QuestionResponse DTO를 생성합니다.
             */
            fun from(question: Question, answer: Answer): QuestionResponse {
                return QuestionResponse(
                    questionId = question.id,
                    title = question.title,
                    description = question.description,
                    type = question.type.name,
                    required = question.required,
                    version = question.version,
                    options = question.options,
                    answers = answer.values
                )
            }
        }
    }

    companion object {
        /**
         * Form과 FormReply 도메인 객체로부터 FormReplyResponse DTO를 생성합니다.
         */
        fun from(form: Form, reply: FormReply): FormReplyResponse {
            val questionResponses = form.questions.list().map { question ->
                val answer = reply.answers.getByQuestionId(question.id)
                QuestionResponse.from(question, answer)
            }

            return FormReplyResponse(
                formReplyId = reply.id,
                formId = reply.formId,
                title = form.title,
                description = form.description,
                questions = questionResponses,
                submittedAt = reply.formattedSubmittedAt()
            )
        }
    }
}
