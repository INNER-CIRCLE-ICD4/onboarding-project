package fc.innercircle.sanghyukonboarding.formreply.domain.service.port

import fc.innercircle.sanghyukonboarding.formreply.domain.model.FormReply

interface FormReplyWriter {
    fun insertOrUpdate(formReply: FormReply): String
}
