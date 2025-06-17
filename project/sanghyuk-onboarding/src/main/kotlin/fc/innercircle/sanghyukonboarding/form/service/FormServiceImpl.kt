package fc.innercircle.sanghyukonboarding.form.service

import fc.innercircle.sanghyukonboarding.common.domain.exception.CustomException
import fc.innercircle.sanghyukonboarding.common.domain.exception.ErrorCode
import fc.innercircle.sanghyukonboarding.form.domain.dto.command.FormCreateCommand
import fc.innercircle.sanghyukonboarding.form.domain.dto.command.FormUpdateCommand
import fc.innercircle.sanghyukonboarding.form.domain.model.Form
import fc.innercircle.sanghyukonboarding.form.interfaces.rest.port.FormService
import fc.innercircle.sanghyukonboarding.form.service.port.FormReader
import fc.innercircle.sanghyukonboarding.form.service.port.FormWriter
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class FormServiceImpl(
    private val writer: FormWriter,
    private val reader: FormReader,
): FormService {

    @Transactional
    override fun create(
        formCreateCommand: FormCreateCommand,
    ): Form {
        val form = Form.from(formCreateCommand)
        val id = writer.insertOrUpdate(form)
        return getById(id)
    }

    override fun getById(id: String): Form {
        return reader.findById(id)
            ?: throw CustomException(ErrorCode.FORM_NOT_FOUND.withArgs(id))
    }

    @Transactional
    override fun update(
        formId: String,
        command: FormUpdateCommand,
    ): Form {
        val form: Form = this.getById(formId)
        val updatedForm = form.updated(command)
        writer.insertOrUpdate(updatedForm)
        return updatedForm
    }
}
