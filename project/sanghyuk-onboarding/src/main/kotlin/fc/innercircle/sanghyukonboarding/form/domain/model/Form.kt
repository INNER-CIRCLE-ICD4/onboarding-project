package fc.innercircle.sanghyukonboarding.form.domain.model

import fc.innercircle.sanghyukonboarding.common.domain.exception.CustomException
import fc.innercircle.sanghyukonboarding.common.domain.exception.ErrorCode
import fc.innercircle.sanghyukonboarding.form.domain.model.validator.FormValidator
import fc.innercircle.sanghyukonboarding.form.domain.model.vo.QuestionTemplates

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

    fun edited(
        title: String,
        description: String,
        questionTemplates: List<QuestionTemplate>,
    ): Form {

        val deletedTemplates: List<QuestionTemplate> = this.questionTemplates.list()
            .filter { template ->
                questionTemplates.none { it.id == template.id }
            }.map { it.deleted() }

        return copy(
            title = title,
            description = description,
            questionTemplates = deletedTemplates + questionTemplates
        )
    }

    fun hasQuestionTemplate(questionTemplateId: String): Boolean {
        return questionTemplates.existsById(questionTemplateId)
    }

    fun getQuestionTemplate(questionTemplateId: String): QuestionTemplate {
        return questionTemplates.getById(questionTemplateId)
    }
}
