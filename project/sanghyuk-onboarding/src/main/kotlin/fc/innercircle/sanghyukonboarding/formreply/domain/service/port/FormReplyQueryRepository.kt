package fc.innercircle.sanghyukonboarding.formreply.domain.service.port

import fc.innercircle.sanghyukonboarding.formreply.domain.model.FormReply

interface FormReplyQueryRepository {
    fun getAllByForm(formId: String): List<FormReply>
}
