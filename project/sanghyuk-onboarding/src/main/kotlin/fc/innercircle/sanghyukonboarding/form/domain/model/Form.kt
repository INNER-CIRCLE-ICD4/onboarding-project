package fc.innercircle.sanghyukonboarding.form.domain.model

import fc.innercircle.sanghyukonboarding.common.domain.exception.CustomException
import fc.innercircle.sanghyukonboarding.common.domain.exception.ErrorCode
import fc.innercircle.sanghyukonboarding.form.domain.model.validator.FormValidator
import fc.innercircle.sanghyukonboarding.form.domain.model.vo.Questions

class Form(
    val id: String = "",
    val title: String,
    val description: String,
    questions: List<Question>,
) {

    val questions: Questions

    init {
        val filteredTemplates: List<Question> = filteringNewOrQuestionTemplateOfThis(questions)
        this.questions = Questions(values = filteredTemplates)
        verifyOrThrowsMaxQuestionCounts(this.questions)
        validateRequiredFields()
    }

    private fun filteringNewOrQuestionTemplateOfThis(questions: List<Question>): List<Question> =
        questions.filter { it ->
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
    private fun verifyOrThrowsMaxQuestionCounts(questions: Questions) {
        if (questions.count() > 10) {
            throw CustomException(ErrorCode.EXCEEDS_MAX_QUESTION_COUNT.withArgs(questions.count()))
        }
    }

    private fun copy(
        id: String = this.id,
        title: String = this.title,
        description: String = this.description,
        questions: List<Question> = this.questions.list()
    ): Form {
        return Form(
            id = id,
            title = title,
            description = description,
            questions = questions
        )
    }

    fun edited(
        title: String,
        description: String,
        newQuestions: List<Question>,
    ): Form {

        // 기존 질문 중 새로 추가된 질문에 없는 질문은 삭제 처리
        val deletedQuestions: List<Question> = this.questions.list()
            .filter { question ->
                newQuestions.none { it.id == question.id }
            }.map { it.deleted() }

        return copy(
            title = title,
            description = description,
            questions = deletedQuestions + newQuestions
        )
    }

    fun hasQuestionTemplate(questionTemplateId: String): Boolean {
        return questions.existsById(questionTemplateId)
    }

    fun getQuestionTemplate(questionTemplateId: String): Question {
        return questions.getById(questionTemplateId)
    }
}
