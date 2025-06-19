package fc.innercircle.sanghyukonboarding.form.domain.model

import fc.innercircle.sanghyukonboarding.common.domain.exception.CustomException
import fc.innercircle.sanghyukonboarding.common.domain.exception.ErrorCode
import fc.innercircle.sanghyukonboarding.form.domain.model.validator.QuestionTemplateValidator
import fc.innercircle.sanghyukonboarding.form.domain.model.vo.InputType
import fc.innercircle.sanghyukonboarding.form.domain.model.vo.QuestionSnapshots

class QuestionTemplate(
    val id: String = "",
    val version: Long = 0L,
    val required: Boolean,
    val displayOrder: Int,
    val deleted: Boolean = false,
    snapshots: List<QuestionSnapshot> = emptyList(),
    val formId: String = ""
) {

    val snapshots: QuestionSnapshots

    init {
        val filteredSnapshots: List<QuestionSnapshot> = filteringNewOrQuestionTemplateOfThis(snapshots)
        this.snapshots = QuestionSnapshots(values = filteredSnapshots)
        validateRequiredFields()
    }

    private fun filteringNewOrQuestionTemplateOfThis(snapshots: List<QuestionSnapshot>): List<QuestionSnapshot> {
        val filteredSnapshots: List<QuestionSnapshot> = snapshots.filter { it ->
            it.isNew() || it.isSnapshotOf(this)
        }
        return filteredSnapshots
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

    override fun hashCode(): Int {
        var result = version.hashCode()
        result = 31 * result + required.hashCode()
        result = 31 * result + displayOrder
        result = 31 * result + deleted.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + formId.hashCode()
        result = 31 * result + snapshots.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is QuestionTemplate) return false

        if (id != other.id) return false
        if (version != other.version) return false
        if (required != other.required) return false
        if (displayOrder != other.displayOrder) return false
        if (deleted != other.deleted) return false
        if (snapshots != other.snapshots) return false
        if (formId != other.formId) return false

        return true
    }

    fun getLatestSnapshot(): QuestionSnapshot {
        return snapshots.list().maxByOrNull { it.version }
            ?: throw IllegalStateException("No snapshots available for question template with id: $id")
    }

    fun edited(
        title: String,
        description: String,
        type: String,
        required: Boolean,
        displayOrder: Int,
        selectableOptions: List<SelectableOption>,
    ): QuestionTemplate {
        val questionSnapshot = QuestionSnapshot(
            title = title,
            description = description,
            type = InputType.valueOrThrows(type),
            version = this.version,
            selectableOptions = selectableOptions,
            questionTemplateId = this.id
        )
        if (this.getLatestSnapshot().isModified(questionSnapshot)) {
            val newVersion: Long = version + 1
            val newSnapshot = QuestionSnapshot(
                title = title,
                description = description,
                type = InputType.valueOrThrows(type),
                version = newVersion,
                selectableOptions = selectableOptions,
                questionTemplateId = this.id
            )
            val copy: QuestionTemplate = this.copy(
                version = newVersion,
                required = required,
                displayOrder = displayOrder,
                snapshots = snapshots.add(newSnapshot)
            )
            return copy
        }
        return this.copy(
            required = required,
            displayOrder = displayOrder,
        )
    }

    fun deleted(): QuestionTemplate {
        return this.copy(
            version = this.version,
            required = this.required,
            displayOrder = this.displayOrder,
            snapshots = snapshots,
            deleted = true
        )
    }

    private fun copy(
        version: Long = this.version,
        required: Boolean = this.required,
        displayOrder: Int = this.displayOrder,
        snapshots: QuestionSnapshots = this.snapshots,
        deleted: Boolean = this.deleted
    ): QuestionTemplate {
        return QuestionTemplate(
            id = this.id,
            version = version,
            required = required,
            displayOrder = displayOrder,
            snapshots = snapshots.list(),
            deleted = deleted,
            formId = this.formId
        )
    }

    fun addSnapshot(
        title: String,
        description: String = "",
        type: String,
        selectableOptions: List<SelectableOption>,
    ): QuestionTemplate {
        val createSnapshot = QuestionSnapshot(
            title = title,
            description = description,
            type = InputType.valueOrThrows(type),
            version = this.version,
            selectableOptions = selectableOptions,
            questionTemplateId = this.id,
        )
        return this.copy(
            snapshots = this.snapshots.add(createSnapshot)
        )
    }
}
