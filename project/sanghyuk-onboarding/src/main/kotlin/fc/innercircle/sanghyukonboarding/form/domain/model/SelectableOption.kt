package fc.innercircle.sanghyukonboarding.form.domain.model

import fc.innercircle.sanghyukonboarding.form.domain.validator.SelectableOptionsValidator

open class SelectableOption(
    val id: String = "",
    val text: String,
    val displayOrder: Int,
    val questionSnapshotId: String = "",
) {

    init {
        validateRequiredFields()
    }

    private fun validateRequiredFields() {
        SelectableOptionsValidator.validateText(text)
        SelectableOptionsValidator.validateDisplayOrder(displayOrder)
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + text.hashCode()
        result = 31 * result + displayOrder
        result = 31 * result + questionSnapshotId.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SelectableOption) return false

        if (id != other.id) return false
        if (text != other.text) return false
        if (displayOrder != other.displayOrder) return false
        if (questionSnapshotId != other.questionSnapshotId) return false

        return true
    }

    companion object {
        fun of(
            value: String,
            displayOrder: Int,
        ): SelectableOption {
            return SelectableOption(
                text = value,
                displayOrder = displayOrder,
            )
        }
    }
}
