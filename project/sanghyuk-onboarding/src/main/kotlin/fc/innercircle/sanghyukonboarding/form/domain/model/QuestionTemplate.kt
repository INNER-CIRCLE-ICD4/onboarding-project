package fc.innercircle.sanghyukonboarding.form.domain.model

import fc.innercircle.sanghyukonboarding.common.domain.exception.CustomException
import fc.innercircle.sanghyukonboarding.common.domain.exception.ErrorCode
import fc.innercircle.sanghyukonboarding.form.domain.dto.command.FormCreateCommand
import fc.innercircle.sanghyukonboarding.form.domain.dto.command.FormUpdateCommand
import fc.innercircle.sanghyukonboarding.form.domain.model.vo.QuestionSnapshots
import fc.innercircle.sanghyukonboarding.form.domain.validator.QuestionTemplateValidator

open class QuestionTemplate(
    val id: String = "",
    val version: Long = 0L,
    val required: Boolean,
    val displayOrder: Int,
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

    fun getLatestSnapshot(): QuestionSnapshot {
        return snapshots.list().maxByOrNull { it.version }
            ?: throw IllegalStateException("No snapshots available for question template with id: $id")
    }

    fun getSnapshotByVersion(version: Long): QuestionSnapshot {
        return snapshots.getByVersion(version)
    }

    fun isModified(questionCmd: FormUpdateCommand.Question): Boolean {
        return this.getLatestSnapshot().isModified(questionCmd)
    }

    fun updatedNewVersion(cmd: FormUpdateCommand.Question, displayOrder: Int): QuestionTemplate {
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
        fun of(cmd: FormCreateCommand.Question, displayOrder: Int): QuestionTemplate {
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
