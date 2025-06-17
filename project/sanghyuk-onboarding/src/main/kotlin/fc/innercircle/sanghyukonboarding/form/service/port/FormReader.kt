package fc.innercircle.sanghyukonboarding.form.service.port

import fc.innercircle.sanghyukonboarding.form.domain.model.Form

interface FormReader {
    fun getById(id: String): Form
}
