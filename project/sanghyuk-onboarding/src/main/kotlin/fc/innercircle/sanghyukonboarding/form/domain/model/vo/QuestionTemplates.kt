package fc.innercircle.sanghyukonboarding.form.domain.model.vo

import fc.innercircle.sanghyukonboarding.form.domain.model.QuestionTemplate

data class QuestionTemplates(
    private val values: List<QuestionTemplate> = emptyList()
) {
    fun list(): List<QuestionTemplate> {
        return values.sortedBy { it.displayOrder }
    }

    fun count(): Long {
        return values.size.toLong()
    }

    fun filter(function: (QuestionTemplate) -> Boolean): QuestionTemplates {
        return QuestionTemplates(values.filter(function))
    }
}
