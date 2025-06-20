package fc.innercircle.sanghyukonboarding.formreply.interfaces.rest.port.dto.request

import fc.innercircle.sanghyukonboarding.formreply.domain.service.dto.param.AnswerParam

data class AnswerRequest(
    val questionTemplateId: String,
    val text: String = "",
    val selectableOptionIds: List<String> = emptyList()
) {
    fun toParam(): AnswerParam {
        return AnswerParam(
            questionTemplateId = questionTemplateId,
            text = text,
            selectableOptionIds = selectableOptionIds
        )
    }
}
