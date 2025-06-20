package fc.innercircle.sanghyukonboarding.formreply.application

import fc.innercircle.sanghyukonboarding.form.domain.model.Form
import fc.innercircle.sanghyukonboarding.form.domain.service.port.FormQueryRepository
import fc.innercircle.sanghyukonboarding.formreply.domain.model.FormReply
import fc.innercircle.sanghyukonboarding.formreply.domain.service.port.FormReplyQueryRepository
import fc.innercircle.sanghyukonboarding.formreply.interfaces.rest.port.SummarizeFormRepliesUseCase
import fc.innercircle.sanghyukonboarding.formreply.interfaces.rest.port.dto.response.ReplySummaryResponse
import jakarta.transaction.Transactional
import org.springframework.stereotype.Component

@Transactional
@Component
class SummarizeFormRepliesFacade(
    private val formQueryRepository: FormQueryRepository,
    private val formReplyQueryRepository: FormReplyQueryRepository
): SummarizeFormRepliesUseCase {
    override fun summarize(formId: String): ReplySummaryResponse {
        val form: Form = formQueryRepository.getById(formId)
        val formReplies: List<FormReply> = formReplyQueryRepository.getAllByForm(form.id)
        return ReplySummaryResponse.of(form, formReplies)
    }
}
