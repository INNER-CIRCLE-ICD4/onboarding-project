package fc.innercircle.sanghyukonboarding.form.interfaces.rest.port

import fc.innercircle.sanghyukonboarding.form.domain.dto.command.FormCreateCommand
import fc.innercircle.sanghyukonboarding.form.domain.dto.command.FormUpdateCommand
import fc.innercircle.sanghyukonboarding.form.domain.model.Form

interface FormService {
    fun create(formCreateCommand: FormCreateCommand, ): Form
    fun getById(id: String): Form
    fun update(formId: String, command: FormUpdateCommand): Form
}
