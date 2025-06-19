package fc.innercircle.sanghyukonboarding.formreply.application

import fc.innercircle.sanghyukonboarding.form.domain.model.Form
import fc.innercircle.sanghyukonboarding.form.service.port.FormQueryRepository
import fc.innercircle.sanghyukonboarding.formreply.domain.dto.command.FormReplyCommand
import fc.innercircle.sanghyukonboarding.formreply.domain.model.FormReply
import fc.innercircle.sanghyukonboarding.formreply.domain.service.FormReplyFactory
import fc.innercircle.sanghyukonboarding.formreply.domain.service.port.FormReplyWriter
import fc.innercircle.sanghyukonboarding.formreply.interfaces.rest.port.SubmitReplyUseCase
import jakarta.transaction.Transactional
import org.springframework.stereotype.Component

@Transactional
@Component
class SubmitReplyFacade(
    private val formQueryRepository: FormQueryRepository,
    private val formReplyFactory: FormReplyFactory,
    private val formReplyWriter: FormReplyWriter,
): SubmitReplyUseCase {

    override fun submit(
        formId: String,
        commands: List<FormReplyCommand>,
    ): String {
        val form: Form = formQueryRepository.getById(formId)
        val formReply: FormReply = formReplyFactory.create(form, commands)
        return formReplyWriter.insertOrUpdate(formReply)
    }
}
