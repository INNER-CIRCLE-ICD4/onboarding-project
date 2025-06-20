package fc.innercircle.sanghyukonboarding.formreply.application

import fc.innercircle.sanghyukonboarding.form.domain.model.Form
import fc.innercircle.sanghyukonboarding.form.domain.service.port.FormQueryRepository
import fc.innercircle.sanghyukonboarding.formreply.domain.service.dto.param.AnswerParam
import fc.innercircle.sanghyukonboarding.formreply.domain.model.FormReply
import fc.innercircle.sanghyukonboarding.formreply.domain.service.ReplyService
import fc.innercircle.sanghyukonboarding.formreply.domain.service.port.FormReplyCommandRepository
import fc.innercircle.sanghyukonboarding.formreply.interfaces.rest.port.SubmitReplyUseCase
import fc.innercircle.sanghyukonboarding.formreply.interfaces.rest.port.dto.request.AnswerRequest
import jakarta.transaction.Transactional
import org.springframework.stereotype.Component

@Transactional
@Component
class SubmitReplyFacade(
    private val formQueryRepository: FormQueryRepository,
    private val replyService: ReplyService,
    private val formReplyCommandRepository: FormReplyCommandRepository,
): SubmitReplyUseCase {

    override fun submit(
        formId: String,
        requests: List<AnswerRequest>,
    ): String {
        val form: Form = formQueryRepository.getById(formId)
        val params: List<AnswerParam> = requests.map { it.toParam() }
        val formReply: FormReply = replyService.reply(form, params)
        return formReplyCommandRepository.insertOrUpdate(formReply)
    }
}
