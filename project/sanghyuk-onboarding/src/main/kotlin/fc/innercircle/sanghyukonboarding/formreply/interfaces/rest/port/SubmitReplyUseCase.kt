package fc.innercircle.sanghyukonboarding.formreply.interfaces.rest.port

import fc.innercircle.sanghyukonboarding.formreply.domain.dto.command.FormReplyCommand

interface SubmitReplyUseCase {
    fun submit(formId: String, commands: List<FormReplyCommand>): String
}
