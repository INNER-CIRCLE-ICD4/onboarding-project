package fc.innercircle.sanghyukonboarding.form.application

import fc.innercircle.sanghyukonboarding.form.domain.service.FormEditor
import fc.innercircle.sanghyukonboarding.form.domain.service.port.FormCommandRepository
import fc.innercircle.sanghyukonboarding.form.domain.service.port.FormQueryRepository
import fc.innercircle.sanghyukonboarding.form.interfaces.rest.port.EditFormUseCase
import fc.innercircle.sanghyukonboarding.form.interfaces.rest.port.dto.request.FormRequest
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Transactional
@Component
class EditFormFacade(
    private val formQueryRepository: FormQueryRepository,
    private val formCommandRepository: FormCommandRepository,
    private val editor: FormEditor,
) : EditFormUseCase {

    override fun edit(formId: String, request: FormRequest) {
        val form = formQueryRepository.getById(formId)
        val criteria = request.questions.map { it.toParam() }
        val editedForm = editor.edit(
            form = form,
            title = request.title,
            description = request.description,
            params = criteria
        )
        formCommandRepository.insertOrUpdate(editedForm)
    }
}
