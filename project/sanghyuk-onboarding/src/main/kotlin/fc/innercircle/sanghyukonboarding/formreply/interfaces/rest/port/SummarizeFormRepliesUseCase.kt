package fc.innercircle.sanghyukonboarding.formreply.interfaces.rest.port

import fc.innercircle.sanghyukonboarding.formreply.interfaces.rest.port.dto.response.ReplySummaryResponse

interface SummarizeFormRepliesUseCase {
    fun summarize(formId: String): ReplySummaryResponse
}
