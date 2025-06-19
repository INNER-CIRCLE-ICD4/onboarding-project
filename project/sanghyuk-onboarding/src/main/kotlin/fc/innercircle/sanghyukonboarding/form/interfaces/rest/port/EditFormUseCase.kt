package fc.innercircle.sanghyukonboarding.form.interfaces.rest.port

import fc.innercircle.sanghyukonboarding.form.interfaces.rest.port.dto.request.FormRequest

interface EditFormUseCase {
    fun edit(formId: String, request: FormRequest)
}
