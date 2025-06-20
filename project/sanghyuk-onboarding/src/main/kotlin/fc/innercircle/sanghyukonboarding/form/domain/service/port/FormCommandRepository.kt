package fc.innercircle.sanghyukonboarding.form.domain.service.port

import fc.innercircle.sanghyukonboarding.form.domain.model.Form

interface FormCommandRepository {
    fun insertOrUpdate(form: Form): String
    fun deleteAll()
}
