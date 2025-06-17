package fc.innercircle.sanghyukonboarding.form.domain.model

import fc.innercircle.sanghyukonboarding.common.domain.exception.CustomException
import fc.innercircle.sanghyukonboarding.common.domain.exception.ErrorCode
import fc.innercircle.sanghyukonboarding.form.domain.dto.command.FormCreateCommand
import fc.innercircle.sanghyukonboarding.form.domain.dto.command.FormUpdateCommand
import fc.innercircle.sanghyukonboarding.form.domain.model.vo.QuestionTemplates
import fc.innercircle.sanghyukonboarding.form.domain.validator.FormValidator

open class Form(
    val id: String = "",
    val title: String,
    val description: String,
    questionTemplates: List<QuestionTemplate>,
) {

    val questionTemplates: QuestionTemplates = QuestionTemplates(
        values = questionTemplates.filter { it.formId == id }
    )

    init {
        validateRequiredFields()
        verifyOrThrowsMaxQuestionCounts(this.questionTemplates)
    }

    private fun validateRequiredFields() {
        FormValidator.validateTitle(title)
        FormValidator.validateDescription(description)
    }

    /**
     * 비즈니스 정책 검사
     * 1. 설문은 최대 10개의 질문을 가질 수 있다.
     */
    private fun verifyOrThrowsMaxQuestionCounts(questionTemplates: QuestionTemplates) {
        if (questionTemplates.count() > 10) {
            throw CustomException(ErrorCode.EXCEEDS_MAX_QUESTION_COUNT.withArgs(questionTemplates.count()))
        }
    }

    private fun copy(
        id: String = this.id,
        title: String = this.title,
        description: String = this.description,
        questionTemplates: List<QuestionTemplate> = this.questionTemplates.list()
    ): Form {
        return Form(
            id = id,
            title = title,
            description = description,
            questionTemplates = questionTemplates
        )
    }

    fun updated(cmd: FormUpdateCommand): Form {
        return this.copy(
            title = cmd.title,
            description = cmd.description,
            questionTemplates = cmd.questions.mapIndexed { idx, questionCmd ->
                val questionTemplate: QuestionTemplate = this.questionTemplates.getById(questionCmd.questionTemplateId)
                replaceIfModified(questionTemplate, questionCmd, idx)
            }
        )
    }

    private fun replaceIfModified(
        questionTemplate: QuestionTemplate,
        questionCmd: FormUpdateCommand.Question,
        idx: Int,
    ): QuestionTemplate = if (questionTemplate.isModified(questionCmd)) {
        questionTemplate.updatedNewVersion(
            cmd = questionCmd,
            displayOrder = idx
        )
    } else {
        questionTemplate
    }

    companion object {
        fun from(cmd: FormCreateCommand): Form {
            val questionTemplates: List<QuestionTemplate> = cmd.questions.mapIndexed { idx, it ->
                QuestionTemplate.of(
                    cmd = it,
                    displayOrder = idx
                )
            }
            return Form(
                title = cmd.title,
                description = cmd.description,
                questionTemplates = questionTemplates
            )
        }
    }
}
