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

    val questionTemplates: QuestionTemplates = QuestionTemplates(
        values = questionTemplates.filter { it ->
            it.formId.isEmpty() || it.formId == id
        }
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

    fun updated(cmd: FormCommand): Form {

        val createdOrUpdatedQuestionTemplates: List<QuestionTemplate> =
            cmd.questions.mapIndexed { idx, questionCmd ->
                when {
                    questionCmd.questionTemplateId.isEmpty() -> {
                        // ID가 비어 있으면 새 질문 생성
                        QuestionTemplate.of(
                            cmd = questionCmd,
                            displayOrder = idx,
                            formId = this.id,
                        )
                    }
                    else -> {
                        // 기존 질문 조회
                        val questionTemplate =
                            this.questionTemplates.getById(questionCmd.questionTemplateId)

                        // 수정 사항이 있으면 updated, 없으면 원본 그대로
                        if (questionTemplate.isModified(questionCmd, idx)) {
                            questionTemplate.updated(
                                cmd = questionCmd,
                                displayOrder = idx
                            )
                        } else {
                            questionTemplate
                        }
                    }
                }
            }
        val createdOrUpdatedQuestionTemplateIds: List<String> = createdOrUpdatedQuestionTemplates.map { it.id }
        val deletedQuestionTemplates: List<QuestionTemplate> = this.questionTemplates.list()
            // 기존 질문 중에서 새 요청에 포함되지 않은 질문들을 찾아 삭제 처리
            .filter { it -> !createdOrUpdatedQuestionTemplateIds.contains(it.id) }
            .map { it.deleted() }

        return this.copy(
            title = cmd.title,
            description = cmd.description,
            questionTemplates = createdOrUpdatedQuestionTemplates + deletedQuestionTemplates
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
