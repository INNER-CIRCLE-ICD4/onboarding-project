package fc.innercircle.sanghyukonboarding.form.domain.model

import fc.innercircle.sanghyukonboarding.form.domain.validator.SelectableOptionsValidator

class SelectableOption(
    val id: String = "",
    val text: String,
    val displayOrder: Int,
    val questionSnapshotId: String = "",
) {

    init {
        validateRequiredFields()
    }

    fun isNew(): Boolean {
        return id.isBlank()
    }

    fun isSelectableOptionOf(snapshot: QuestionSnapshot): Boolean {
        return this.questionSnapshotId == snapshot.id
    }

    private fun validateRequiredFields() {
        SelectableOptionsValidator.validateText(text)
        SelectableOptionsValidator.validateDisplayOrder(displayOrder)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SelectableOption

        if (displayOrder != other.displayOrder) return false
        if (text != other.text) return false
        if (questionSnapshotId != other.questionSnapshotId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = displayOrder
        result = 31 * result + text.hashCode()
        result = 31 * result + questionSnapshotId.hashCode()
        return result
    }


    companion object {
        fun of(
            id: String = "",
            text: String,
            displayOrder: Int,
            questionSnapshotId: String = "",
        ): SelectableOption {
            return SelectableOption(
                id = id,
                text = text,
                displayOrder = displayOrder,
                questionSnapshotId = questionSnapshotId,
            )
        }
    }
}
