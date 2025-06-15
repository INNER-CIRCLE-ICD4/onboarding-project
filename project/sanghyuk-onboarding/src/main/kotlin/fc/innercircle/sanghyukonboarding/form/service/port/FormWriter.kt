package fc.innercircle.sanghyukonboarding.form.service.port

import fc.innercircle.sanghyukonboarding.form.domain.model.Form

interface FormWriter {
    fun insertOrUpdate(form: Form): String
}
