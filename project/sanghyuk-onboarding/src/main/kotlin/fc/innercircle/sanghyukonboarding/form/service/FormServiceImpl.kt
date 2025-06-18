package fc.innercircle.sanghyukonboarding.form.service

import fc.innercircle.sanghyukonboarding.form.domain.dto.command.FormCommand
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
        command: FormCommand
    ): String {
        val form: Form = Form.from(command)
        return writer.insertOrUpdate(form)
    }

    override fun getById(id: String): Form {
        return reader.getById(id)
    }

    @Transactional
    override fun update(
        formId: String,
        command: FormCommand,
    ): String {
        val form: Form = this.getById(formId)
        val updatedForm = form.updated(command)
        return writer.insertOrUpdate(updatedForm)
    }
}
