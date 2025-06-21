package fc.innercircle.sanghyukonboarding.formreply.interfaces.rest.port

import fc.innercircle.sanghyukonboarding.formreply.interfaces.rest.port.dto.request.AnswerRequest

interface SubmitReplyUseCase {
    fun submit(formId: String, requests: List<AnswerRequest>): String
}
