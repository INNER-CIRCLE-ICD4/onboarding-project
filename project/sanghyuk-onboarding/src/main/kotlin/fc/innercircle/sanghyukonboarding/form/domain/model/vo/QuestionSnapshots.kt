package fc.innercircle.sanghyukonboarding.form.domain.model.vo

import fc.innercircle.sanghyukonboarding.form.domain.model.QuestionSnapshot

data class QuestionSnapshots(
    private val values: List<QuestionSnapshot> = emptyList()
) {

    fun list(): List<QuestionSnapshot> {
        return values
    }

    fun filter(predicate: (QuestionSnapshot) -> Boolean): QuestionSnapshots {
        return QuestionSnapshots(
            values = values.filter(predicate)
        )
    }
}
