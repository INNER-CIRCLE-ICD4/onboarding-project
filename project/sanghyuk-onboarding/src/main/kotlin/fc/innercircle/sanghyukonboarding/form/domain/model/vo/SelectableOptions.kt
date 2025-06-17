package fc.innercircle.sanghyukonboarding.form.domain.model.vo

import fc.innercircle.sanghyukonboarding.form.domain.model.SelectableOption

data class SelectableOptions(
    private val values: List<SelectableOption> = emptyList()
) {
    fun list(): List<SelectableOption> {
        return values.sortedBy { it.displayOrder }
    }

    fun filter(predicate: (SelectableOption) -> Boolean): SelectableOptions {
        return SelectableOptions(
            values = values.filter(predicate)
        )
    }

    fun size(): Long = values.size.toLong()
}
