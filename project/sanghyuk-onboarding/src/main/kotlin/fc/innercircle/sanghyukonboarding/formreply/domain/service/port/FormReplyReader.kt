package fc.innercircle.sanghyukonboarding.formreply.domain.service.port

import fc.innercircle.sanghyukonboarding.formreply.domain.model.FormReply

interface FormReplyReader {
    fun getAllByFormId(formId: String): List<FormReply>
}
