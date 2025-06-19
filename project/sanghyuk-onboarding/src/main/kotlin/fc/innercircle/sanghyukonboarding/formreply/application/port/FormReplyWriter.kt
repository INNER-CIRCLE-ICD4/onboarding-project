package fc.innercircle.sanghyukonboarding.formreply.application.port

import fc.innercircle.sanghyukonboarding.formreply.domain.model.FormReply

interface FormReplyWriter {
    fun insertOrUpdate(formReply: FormReply): String
}
