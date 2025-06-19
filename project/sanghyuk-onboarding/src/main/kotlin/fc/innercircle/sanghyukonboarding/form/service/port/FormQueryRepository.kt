package fc.innercircle.sanghyukonboarding.form.service.port

import fc.innercircle.sanghyukonboarding.form.domain.model.Form

interface FormQueryRepository {
    fun getById(id: String): Form
}
