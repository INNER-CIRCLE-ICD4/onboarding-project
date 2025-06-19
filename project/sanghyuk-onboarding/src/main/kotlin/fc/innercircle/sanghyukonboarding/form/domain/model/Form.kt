package fc.innercircle.sanghyukonboarding.form.domain.model

import fc.innercircle.sanghyukonboarding.common.domain.exception.CustomException
import fc.innercircle.sanghyukonboarding.common.domain.exception.ErrorCode
import fc.innercircle.sanghyukonboarding.form.domain.dto.command.FormCommand
import fc.innercircle.sanghyukonboarding.form.domain.model.vo.QuestionTemplates
import fc.innercircle.sanghyukonboarding.form.domain.validator.FormValidator

class Form(
    val id: String = "",
    val title: String,
    val description: String,
    questionTemplates: List<QuestionTemplate>,
) {

    val questionTemplates: QuestionTemplates

    init {
        val filteredTemplates: List<QuestionTemplate> = filteringNewOrQuestionTemplateOfThis(questionTemplates)
        this.questionTemplates = QuestionTemplates(values = filteredTemplates)
        verifyOrThrowsMaxQuestionCounts(this.questionTemplates)
        validateRequiredFields()
    }

    private fun filteringNewOrQuestionTemplateOfThis(questionTemplates: List<QuestionTemplate>): List<QuestionTemplate> =
        questionTemplates.filter { it ->
            it.isNew() || it.isQuestionOf(this)
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

    fun updated(cmd: FormCommand): Form {
        // 1) 생성 혹은 업데이트된 템플릿 모으기
        val updatedTemplates = cmd.questions.mapIndexed { idx, questionCmd ->
            when {
                questionCmd.questionTemplateId.isBlank() ->
                    // 새 질문
                    QuestionTemplate.of(
                        cmd = questionCmd,
                        displayOrder = idx,
                        formId = this.id
                    )
                else ->
                    // 기존 질문 업데이트
                    this.questionTemplates
                        .getById(questionCmd.questionTemplateId)
                        .updated(questionCmd, idx)
            }
        }

        // 2) cmd에 없는 기존 질문은 삭제 처리
        val deletedTemplates = this.questionTemplates.list().filter { template ->
            !updatedTemplates.any { it.id == template.id }
        }.map { it.deleted() }

        // 3) 새로운 질문 리스트 + 삭제된 질문 리스트 합쳐서 copy
        return copy(
            title = cmd.title,
            description = cmd.description,
            questionTemplates = updatedTemplates + deletedTemplates
        )
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
