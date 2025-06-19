package fc.innercircle.sanghyukonboarding.form.interfaces.rest.port

import fc.innercircle.sanghyukonboarding.form.interfaces.rest.port.dto.request.FormRequest

interface CreateFormUseCase {
    fun create(request: FormRequest): String
}
