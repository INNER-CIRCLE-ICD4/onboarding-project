package fc.innercircle.sanghyukonboarding.formreply.domain.service

import fc.innercircle.sanghyukonboarding.common.domain.exception.CustomException
import fc.innercircle.sanghyukonboarding.common.domain.exception.ErrorCode
import fc.innercircle.sanghyukonboarding.common.domain.service.ClockHolder
import fc.innercircle.sanghyukonboarding.form.domain.model.Form
import fc.innercircle.sanghyukonboarding.form.domain.model.QuestionSnapshot
import fc.innercircle.sanghyukonboarding.form.domain.model.QuestionTemplate
import fc.innercircle.sanghyukonboarding.form.domain.model.vo.InputType
import fc.innercircle.sanghyukonboarding.formreply.domain.dto.command.FormReplyCommand
import fc.innercircle.sanghyukonboarding.formreply.domain.model.Answer
import fc.innercircle.sanghyukonboarding.formreply.domain.model.FormReply
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class FormReplyFactory(private val clockHolder: ClockHolder) {

    fun create(form: Form, commands: List<FormReplyCommand>): FormReply {
        validateQuestionSizeEquals(form, commands)
        val answers = generateAnswers(commands, form)
        val now: LocalDateTime = clockHolder.localDateTime()
        return FormReply(
            formId = form.id,
            answers = answers,
            submittedAt = now
        )
    }

    private fun generateAnswers(
        commands: List<FormReplyCommand>,
        form: Form,
    ): List<Answer> {
        val answers = commands.map { command ->
            val questionTemplateId: String = command.questionTemplateId
            val questionTemplate: QuestionTemplate = form.questionTemplates.getById(questionTemplateId)
            createAnswerByType(command, questionTemplate)
        }
        return answers
    }

    private fun createAnswerByType(
        command: FormReplyCommand,
        questionTemplate: QuestionTemplate
    ): Answer {
        val lastQuestionSnapshot: QuestionSnapshot = questionTemplate.getLatestSnapshot()
        validateSelectableOptionsIdAllInclude(lastQuestionSnapshot, command)
        validateInputTypeAndAnswer(lastQuestionSnapshot.type, command)
        return Answer(
            text = command.text,
            selectableOptionIds = command.selectableOptionIds,
            questionSnapshotId = lastQuestionSnapshot.id,
        )
    }

    /**
     * 정책: 설문에 대한 답변은 설문에 포함된 질문의 개수와 일치해야 한다.
     */
    private fun validateQuestionSizeEquals(
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
    private fun validateSelectableOptionsIdAllInclude(
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

    /**
     * 비즈니스 정책 검사
     * 1. 텍스트형 질문의 답변은 비어있을 수 없다.
     * 2. 선택형 질문의 답변은 최소 하나 이상의 선택지를 포함해야 한다.
     * 3. 단일 선택형 질문의 답변은 최대 하나의 선택지만 포함해야 한다.
     */
    private fun validateInputTypeAndAnswer(questionInputType: InputType, command: FormReplyCommand) {
        when {
            command.text.isNotBlank() && command.selectableOptionIds.isNotEmpty() -> {
                throw CustomException(ErrorCode.INVALID_ANSWER.withArgs(questionInputType))
            }
            questionInputType.isTextType() && command.text.isBlank() -> {
                throw CustomException(ErrorCode.INVALID_TEXT_QUESTION_ANSWER.withArgs(questionInputType))
            }
            questionInputType.isSelectableType() && command.selectableOptionIds.isEmpty() -> {
                throw CustomException(ErrorCode.INVALID_SELECTABLE_QUESTION_ANSWER.withArgs(questionInputType))
            }
            questionInputType.isSingleSelectableType() && command.selectableOptionIds.size > 1 -> {
                throw CustomException(ErrorCode.INVALID_SINGLE_SELECTABLE_QUESTION_ANSWER.withArgs(questionInputType))
            }
        }
    }

}
