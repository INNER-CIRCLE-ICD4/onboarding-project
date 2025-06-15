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
