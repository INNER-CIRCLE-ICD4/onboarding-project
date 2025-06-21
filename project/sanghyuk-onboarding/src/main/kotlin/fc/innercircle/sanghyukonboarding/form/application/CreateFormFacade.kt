package fc.innercircle.sanghyukonboarding.form.application

import fc.innercircle.sanghyukonboarding.form.domain.model.Form
import fc.innercircle.sanghyukonboarding.form.domain.service.FormEditor
import fc.innercircle.sanghyukonboarding.form.domain.service.dto.param.QuestionParam
import fc.innercircle.sanghyukonboarding.form.domain.service.port.FormCommandRepository
import fc.innercircle.sanghyukonboarding.form.interfaces.rest.port.CreateFormUseCase
import fc.innercircle.sanghyukonboarding.form.interfaces.rest.port.dto.request.FormRequest
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Transactional
@Component
class CreateFormFacade(
    private val formCommandRepository: FormCommandRepository,
    private val editor: FormEditor,
) : CreateFormUseCase {

    override fun create(request: FormRequest): String {
        val criteria: List<QuestionParam> = request.questions.map(FormRequest.QuestionRequest::toParam)
        val form: Form = editor.newForm(
            title = request.title,
            description = request.description,
            params = criteria
        )
        return formCommandRepository.insertOrUpdate(form)
    }
}
