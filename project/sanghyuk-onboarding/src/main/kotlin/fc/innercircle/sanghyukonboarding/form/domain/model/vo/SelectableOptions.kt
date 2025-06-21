package fc.innercircle.sanghyukonboarding.form.domain.model.vo

import fc.innercircle.sanghyukonboarding.form.domain.model.SelectableOption

class SelectableOptions(
    values: List<SelectableOption> = emptyList(),
) {

    private val values: List<SelectableOption> = values.sortedBy { it.displayOrder }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SelectableOptions

        return values == other.values
    }

    override fun hashCode(): Int {
        return values.hashCode()
    }

    fun list(): List<SelectableOption> {
        return values.sortedBy { it.displayOrder }
    }

    fun filter(predicate: (SelectableOption) -> Boolean): SelectableOptions {
        return SelectableOptions(
            values = values.filter(predicate)
        )
    }

    fun size(): Long = values.size.toLong()
    fun containsAll(selectableOptionIds: List<String>): Boolean {
        val ids = values.map { it.id }
        return ids.containsAll(selectableOptionIds)
    }
}
