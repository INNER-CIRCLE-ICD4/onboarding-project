package fc.innercircle.sanghyukonboarding.form.service

import fc.innercircle.sanghyukonboarding.common.domain.exception.CustomException
import fc.innercircle.sanghyukonboarding.common.domain.exception.ErrorCode
import fc.innercircle.sanghyukonboarding.form.domain.dto.command.FormCommand
import fc.innercircle.sanghyukonboarding.form.domain.model.Form
import fc.innercircle.sanghyukonboarding.form.interfaces.rest.port.FormService
import fc.innercircle.sanghyukonboarding.form.service.port.FormRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FormServiceImpl(
    private val repository: FormRepository
): FormService {

    @Transactional
    override fun create(
        formCommand: FormCommand,
    ): Form {
        val form = Form.from(formCommand)
        return repository.insertOrUpdate(form)
    }

    @Transactional(readOnly = true)
    override fun getById(id: String): Form {
        return repository.findById(id)
            ?: throw CustomException(ErrorCode.FORM_NOT_FOUND.withArgs(id))
    }
}
