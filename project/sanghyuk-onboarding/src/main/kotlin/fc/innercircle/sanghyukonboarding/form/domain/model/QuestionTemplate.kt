package fc.innercircle.sanghyukonboarding.form.domain.model

import fc.innercircle.sanghyukonboarding.common.domain.exception.CustomException
import fc.innercircle.sanghyukonboarding.common.domain.exception.ErrorCode
import fc.innercircle.sanghyukonboarding.form.domain.dto.command.FormCommand
import fc.innercircle.sanghyukonboarding.form.domain.model.vo.QuestionSnapshots
import fc.innercircle.sanghyukonboarding.form.domain.validator.QuestionTemplateValidator

open class QuestionTemplate(
    val id: String = "",
    val version: Long = 0L,
    val required: Boolean,
    val displayOrder: Int,
    val deleted: Boolean = false,
    snapshots: List<QuestionSnapshot> = emptyList(),
    val formId: String = ""
) {

    val snapshots: QuestionSnapshots = QuestionSnapshots(
        values = snapshots.filter { it.questionTemplateId == id }
    )

    init {
        validateRequiredFields()
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

    fun getSnapshotByVersion(version: Long): QuestionSnapshot {
        return snapshots.getByVersion(version)
    }

    fun isModified(questionCmd: FormCommand.Question, displayOrder: Int): Boolean {
        val questionTemplate: QuestionTemplate = QuestionTemplate.of(questionCmd, displayOrder)
        return this != questionTemplate
    }

    fun updated(cmd: FormCommand.Question, displayOrder: Int): QuestionTemplate {
        isLatestVersionOrThrows(cmd.version)
        val currentVersion: Long = cmd.version
        val snapshot: QuestionSnapshot = this.getSnapshotByVersion(currentVersion)
        if (snapshot.isModified(cmd)) {
            val newVersion: Long = currentVersion + 1
            val newSnapshot: QuestionSnapshot = QuestionSnapshot.of(cmd, newVersion)
            return this.copy(
                version = newVersion,
                required = cmd.required,
                displayOrder = displayOrder,
                snapshots = snapshots.add(newSnapshot)
            )
        }
        return this
    }

    fun deleted(): QuestionTemplate {
        isLatestVersionOrThrows(version)
        return this.copy(
            version = this.version,
            required = this.required,
            displayOrder = this.displayOrder,
            snapshots = snapshots
        )
    }

    private fun isLatestVersionOrThrows(version: Long) {
        if (version != this.version) {
            throw CustomException(
                ErrorCode.NOT_MODIFIABLE_QUESTION_TEMPLATE_VERSION.withArgs(
                    version,
                    this.version
                )
            )
        }
    }

    private fun copy(
        version: Long,
        required: Boolean,
        displayOrder: Int,
        snapshots: QuestionSnapshots,
    ): QuestionTemplate {
        return QuestionTemplate(
            id = this.id,
            version = version,
            required = required,
            displayOrder = displayOrder,
            snapshots = snapshots.list(),
            formId = this.formId
        )
    }

    companion object {
        fun of(cmd: FormCommand.Question, displayOrder: Int): QuestionTemplate {
            val questionSnapshots: List<QuestionSnapshot> = listOf(
                QuestionSnapshot.of(cmd)
            )
            return QuestionTemplate(
                version = 0L,
                required = cmd.required,
                displayOrder = displayOrder,
                snapshots = questionSnapshots,
            )
        }
    }
}
