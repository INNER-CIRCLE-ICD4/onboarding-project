package fc.innercircle.sanghyukonboarding.form.service.port

import fc.innercircle.sanghyukonboarding.form.domain.model.Form

interface FormRepository {
    fun insertOrUpdate(form: Form): Form
    fun findById(id: String): Form?
}
