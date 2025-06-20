package fc.innercircle.sanghyukonboarding.formreply.domain.service.port

import fc.innercircle.sanghyukonboarding.formreply.domain.model.FormReply

interface FormReplyCommandRepository {
    fun insertOrUpdate(formReply: FormReply): String
}
