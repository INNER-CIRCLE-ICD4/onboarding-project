package fc.innercircle.sanghyukonboarding.form.service.port

import fc.innercircle.sanghyukonboarding.form.domain.model.Form

interface FormReader {
    fun findById(id: String): Form?
}
