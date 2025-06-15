package fc.innercircle.sanghyukonboarding.form.interfaces.rest.port

import fc.innercircle.sanghyukonboarding.form.domain.dto.command.FormCommand
import fc.innercircle.sanghyukonboarding.form.domain.model.Form

interface FormService {
    fun create(
        formCommand: FormCommand,
    ): Form

    fun getById(id: String): Form
}
