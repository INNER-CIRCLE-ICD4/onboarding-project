package fc.innercircle.sanghyukonboarding.form.domain.model.vo

import fc.innercircle.sanghyukonboarding.common.domain.exception.CustomException
import fc.innercircle.sanghyukonboarding.common.domain.exception.ErrorCode
import fc.innercircle.sanghyukonboarding.form.domain.model.Question

class Questions(values: List<Question> = emptyList()) {

    private val values: List<Question> = values.sortedBy { it.displayOrder }

    fun list(): List<Question> {
        return values.sortedBy { it.displayOrder }
    }

    fun count(): Long {
        return values.size.toLong()
    }

    fun filter(function: (Question) -> Boolean): Questions {
        return Questions(values.filter(function))
    }

    fun size(): Int = values.size
    fun getById(id: String): Question {
        return values.firstOrNull { it.id == id }
            ?: throw CustomException(ErrorCode.QUESTION_TEMPLATE_NOT_FOUND.withArgs(id))
    }

    fun existsById(questionTemplateId: String): Boolean {
        return values.any { it.id == questionTemplateId }
    }
}
