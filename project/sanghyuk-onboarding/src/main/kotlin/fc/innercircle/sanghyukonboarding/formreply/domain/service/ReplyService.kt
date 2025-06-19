package fc.innercircle.sanghyukonboarding.formreply.domain.service

import fc.innercircle.sanghyukonboarding.common.domain.exception.CustomException
import fc.innercircle.sanghyukonboarding.common.domain.exception.ErrorCode
import fc.innercircle.sanghyukonboarding.form.domain.model.Form
import fc.innercircle.sanghyukonboarding.form.domain.model.QuestionSnapshot
import fc.innercircle.sanghyukonboarding.form.domain.model.QuestionTemplate
import fc.innercircle.sanghyukonboarding.formreply.application.port.FormReplyWriter
import fc.innercircle.sanghyukonboarding.formreply.domain.dto.command.FormReplyCommand
import fc.innercircle.sanghyukonboarding.formreply.domain.model.Answer
import org.springframework.stereotype.Service

@Service
class ReplyService(
    private val formReplyWriter: FormReplyWriter,
) {

    fun submitReply(form: Form, commands: List<FormReplyCommand>): String {

        verifyQuestionSizeEqualsOrThrows(form, commands)

        // Create answers from commands
        val answers = commands.map { command ->
            val questionTemplateId: String = command.questionTemplateId
            val questionTemplate: QuestionTemplate = form.questionTemplates.getById(questionTemplateId)
            createAnswerByType(command, questionTemplate)
        }

        // Create a FormReply object
        val formReply = FormReply(
            formId = form.id,
            answers = answers,
            submittedAt = clockHolder.localDateTime()
        )

        // Save the FormReply and return its ID
        return formReplyWriter.insertOrUpdate(formReply)

    }

    private fun createAnswerByType(
        command: FormReplyCommand,
        questionTemplate: QuestionTemplate
    ): Answer {
        val lastQuestionSnapshot: QuestionSnapshot = questionTemplate.getLatestSnapshot()
        verifySelectableOptionsIdAllIncludeOrThrows(lastQuestionSnapshot, command)
        return Answer(
            questionInputType = lastQuestionSnapshot.type,
            answer = command.answer,
            selectableOptionIds = command.selectableOptionIds,
            questionSnapshotId = lastQuestionSnapshot.id,
        )
    }

    /**
     * 정책: 설문에 대한 답변은 설문에 포함된 질문의 개수와 일치해야 한다.
     */
    private fun verifyQuestionSizeEqualsOrThrows(
        form: Form,
        commands: List<FormReplyCommand>,
    ) {
        if (form.questionTemplates.size() != commands.size) {
            throw CustomException(
                ErrorCode.INVALID_ANSWER_SIZE.withArgs(
                    form.id,
                    form.questionTemplates.size(),
                    commands.size
                )
            )
        }
    }

    /**
     * 정책: 선택 가능한 옵션 ID는 해당 질문의 스냅샷에 포함된 옵션 ID와 일치해야 한다.
     */
    private fun verifySelectableOptionsIdAllIncludeOrThrows(
        lastQuestionSnapshot: QuestionSnapshot,
        command: FormReplyCommand,
    ) {
        val selectableOptionIds: List<String> = lastQuestionSnapshot.selectableOptionIds()
        val notIncludeSelectableOptionIds: List<String> =
            command.selectableOptionIds.filter { it !in selectableOptionIds }
        if (notIncludeSelectableOptionIds.isNotEmpty()) {
            throw CustomException(
                ErrorCode.INVALID_SELECTABLE_OPTION.withArgs(
                    command.questionTemplateId,
                    notIncludeSelectableOptionIds.joinToString(", ")
                )
            )
        }
    }


}
