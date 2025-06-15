package fc.innercircle.sanghyukonboarding.form.service

import fc.innercircle.sanghyukonboarding.common.domain.exception.CustomException
import fc.innercircle.sanghyukonboarding.common.domain.exception.ErrorCode
import fc.innercircle.sanghyukonboarding.form.domain.dto.command.FormCommand
import fc.innercircle.sanghyukonboarding.form.domain.model.Form
import fc.innercircle.sanghyukonboarding.form.interfaces.rest.port.FormService
import fc.innercircle.sanghyukonboarding.form.service.port.FormReader
import fc.innercircle.sanghyukonboarding.form.service.port.FormWriter
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FormServiceImpl(
    private val writer: FormWriter,
    private val reader: FormReader,
): FormService {

    @Transactional
    override fun create(
        formCommand: FormCommand,
    ): Form {
        val form = Form.from(formCommand)
        val id = writer.insertOrUpdate(form)
        return getById(id)
    }

    @Transactional(readOnly = true)
    override fun getById(id: String): Form {
        return reader.findById(id)
            ?: throw CustomException(ErrorCode.FORM_NOT_FOUND.withArgs(id))
    }
}
