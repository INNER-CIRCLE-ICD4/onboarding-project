package fc.innercircle.sanghyukonboarding.formreply.application.port

import fc.innercircle.sanghyukonboarding.formreply.domain.model.FormReply

interface FormReplyReader {
    fun getById(formReplyId: String): FormReply
}
