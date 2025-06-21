package fc.innercircle.sanghyukonboarding.formreply.interfaces.rest.port.dto.request

import fc.innercircle.sanghyukonboarding.formreply.domain.service.dto.param.AnswerParam

data class AnswerRequest(
    val questionId: String,
    val values: List<String>,
) {
    fun toParam(): AnswerParam {
        return AnswerParam(
            questionId = questionId,
            values = values.toList()
        )
    }
}
