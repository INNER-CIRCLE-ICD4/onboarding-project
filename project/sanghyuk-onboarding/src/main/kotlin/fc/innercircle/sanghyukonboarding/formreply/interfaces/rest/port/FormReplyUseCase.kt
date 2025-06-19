package fc.innercircle.sanghyukonboarding.formreply.interfaces.rest.port

import fc.innercircle.sanghyukonboarding.formreply.domain.dto.command.FormReplyCommand

interface FormReplyUseCase {
    fun reply(formId: String, cmds: List<FormReplyCommand>): String
}
