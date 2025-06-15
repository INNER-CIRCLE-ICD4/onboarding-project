package fc.innercircle.sanghyukonboarding.form.domain.model

import fc.innercircle.sanghyukonboarding.common.domain.exception.CustomException
import fc.innercircle.sanghyukonboarding.common.domain.exception.ErrorCode
import fc.innercircle.sanghyukonboarding.form.domain.dto.command.FormCommand
import fc.innercircle.sanghyukonboarding.form.domain.model.vo.QuestionTemplates
import fc.innercircle.sanghyukonboarding.form.domain.validator.FormValidator

open class Form(
    val id: String = "",
    val title: String,
    val description: String,
    questionTemplates: List<QuestionTemplate>
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

    companion object {
        fun from(cmd: FormCommand): Form {
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
