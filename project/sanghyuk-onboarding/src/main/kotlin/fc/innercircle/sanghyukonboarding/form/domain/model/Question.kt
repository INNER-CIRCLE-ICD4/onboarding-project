package fc.innercircle.sanghyukonboarding.form.domain.model

import fc.innercircle.sanghyukonboarding.common.domain.exception.CustomException
import fc.innercircle.sanghyukonboarding.common.domain.exception.ErrorCode
import fc.innercircle.sanghyukonboarding.form.domain.model.validator.QuestionTemplateValidator
import fc.innercircle.sanghyukonboarding.form.domain.model.vo.InputType

class Question(
    val id: String = "",
    val version: Long = 0L,
    val required: Boolean,
    val type: InputType,
    val displayOrder: Int,
    val title: String,
    val description: String = "",
    val deleted: Boolean = false,
    val options: List<String> = emptyList(),
    val formId: String = "",
) {

    init {
        if (type.isTextType() && options.isNotEmpty()) {
            throw CustomException(ErrorCode.TEXT_TYPE_INPUT_ERROR.withArgs(title))
        }
        if (type.isSelectableType() && options.isEmpty()) {
            throw CustomException(ErrorCode.SELECTABLE_TYPE_INPUT_ERROR.withArgs(title))
        }
        validateRequiredFields()
    }

    fun isNew(): Boolean {
        return id.isBlank()
    }

    fun isQuestionOf(form: Form): Boolean {
        return this.formId == form.id
    }

    private fun validateRequiredFields() {
        QuestionTemplateValidator.validateVersion(version)
        QuestionTemplateValidator.validateDisplayOrder(displayOrder)
    }

    fun revise(
        required: Boolean,
        displayOrder: Int,
        type: String,
        title: String,
        description: String,
        options: List<String>,
    ): Question {
        val copyQuestion: Question = this.copy(
            required = required,
            displayOrder = displayOrder,
            type = InputType.valueOrThrows(type),
            title = title,
            description = description,
            options = options
        )

        if (isModified(copyQuestion)) {
            return this.copy(
                version = this.version + 1,
                required = copyQuestion.required,
                displayOrder = copyQuestion.displayOrder,
                type = copyQuestion.type,
                title = copyQuestion.title,
                description = copyQuestion.description,
                options = copyQuestion.options
            )
        }
        return copyQuestion
    }

    private fun isModified(question: Question): Boolean {
        return this != question
    }

    fun deleted(): Question {
        return this.copy(deleted = true)
    }

    private fun copy(
        version: Long = this.version,
        required: Boolean = this.required,
        displayOrder: Int = this.displayOrder,
        type: InputType = this.type,
        title: String = this.title,
        description: String = this.description,
        options: List<String> = this.options,
        deleted: Boolean = this.deleted,
    ): Question {
        return Question(
            id = this.id,
            version = version,
            required = required,
            displayOrder = displayOrder,
            type = type,
            title = title,
            description = description,
            options = options,
            deleted = deleted,
            formId = this.formId
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Question

        if (version != other.version) return false
        if (required != other.required) return false
        if (displayOrder != other.displayOrder) return false
        if (deleted != other.deleted) return false
        if (id != other.id) return false
        if (type != other.type) return false
        if (title != other.title) return false
        if (description != other.description) return false
        if (options != other.options) return false
        if (formId != other.formId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = version.hashCode()
        result = 31 * result + required.hashCode()
        result = 31 * result + displayOrder
        result = 31 * result + deleted.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + options.hashCode()
        result = 31 * result + formId.hashCode()
        return result
    }
}
