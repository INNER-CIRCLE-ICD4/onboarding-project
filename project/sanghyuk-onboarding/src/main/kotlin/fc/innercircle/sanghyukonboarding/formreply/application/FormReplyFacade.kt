package fc.innercircle.sanghyukonboarding.formreply.application

import fc.innercircle.sanghyukonboarding.common.domain.exception.CustomException
import fc.innercircle.sanghyukonboarding.common.domain.exception.ErrorCode
import fc.innercircle.sanghyukonboarding.common.service.ClockHolder
import fc.innercircle.sanghyukonboarding.form.domain.model.Form
import fc.innercircle.sanghyukonboarding.form.domain.model.QuestionSnapshot
import fc.innercircle.sanghyukonboarding.form.domain.model.QuestionTemplate
import fc.innercircle.sanghyukonboarding.form.service.port.FormReader
import fc.innercircle.sanghyukonboarding.formreply.application.port.FormReplyReader
import fc.innercircle.sanghyukonboarding.formreply.domain.dto.command.FormReplyCommand
import fc.innercircle.sanghyukonboarding.formreply.domain.model.Answer
import fc.innercircle.sanghyukonboarding.formreply.domain.model.FormReply
import fc.innercircle.sanghyukonboarding.formreply.interfaces.rest.port.FormReplyUseCase
import jakarta.transaction.Transactional
import org.springframework.stereotype.Component

@Transactional
@Component
class FormReplyFacade(
    private val formReader: FormReader,
    private val formReplyReader: FormReplyReader,
    private val clockHolder: ClockHolder
): FormReplyUseCase {

    override fun reply(
        formId: String,
        cmds: List<FormReplyCommand>,
    ): String {
        val form: Form = formReader.getById(formId)
        // 정책: 설문에 대한 답변은 설문에 포함된 질문의 개수와 일치해야 한다.

        // 답변 생성
        val answers: List<Answer> = cmds.map { command ->
            val questionTemplate: QuestionTemplate = form.questionTemplates.getById(command.questionTemplateId)
            createAnswerByType(command, questionTemplate)
        }
        val formReply = FormReply(
            formId = formId,
            answers = answers,
            submittedAt = clockHolder.localDateTime()
        )
        val formReplyId = formReplyWriter.insertOrUpdate(formReply)
        return formReplyId
    }
}
