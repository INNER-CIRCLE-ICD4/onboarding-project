package fc.innercircle.sanghyukonboarding.form.domain.model

import fc.innercircle.sanghyukonboarding.form.domain.dto.command.FormCommand
import fc.innercircle.sanghyukonboarding.form.domain.model.vo.InputType
import fc.innercircle.sanghyukonboarding.form.domain.model.vo.SelectableOptions
import fc.innercircle.sanghyukonboarding.form.domain.validator.QuestionSnapshotValidator

open class QuestionSnapshot(
    val id: String = "",
    val title: String,
    val description: String,
    val type: InputType,
    val version: Long,
    selectableOptions: List<SelectableOption>,
    val questionTemplateId: String = ""
) {

    val selectableOptions: SelectableOptions = SelectableOptions(
        values = selectableOptions.filter { it.questionSnapshotId == id }
    )

    init {
        validateRequiredFields()
    }

    private fun validateRequiredFields() {
        QuestionSnapshotValidator.validateTitle(title)
        QuestionSnapshotValidator.validateDescription(description)
        QuestionSnapshotValidator.validateVersion(version)
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + version.hashCode()
        result = 31 * result + selectableOptions.hashCode()
        result = 31 * result + questionTemplateId.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is QuestionSnapshot) return false

        if (id != other.id) return false
        if (title != other.title) return false
        if (description != other.description) return false
        if (type != other.type) return false
        if (version != other.version) return false
        if (selectableOptions != other.selectableOptions) return false
        if (questionTemplateId != other.questionTemplateId) return false

        return true
    }

    fun isModified(cmd: FormCommand.Question): Boolean {
        val newSnapshot: QuestionSnapshot = QuestionSnapshot.of(cmd)
        return this != newSnapshot
    }

    companion object {
        fun of(cmd: FormCommand.Question): QuestionSnapshot {
            val selectableOptions: List<SelectableOption> = cmd.selectableOptions.mapIndexed { idx, option ->
                SelectableOption.of(
                    value = option.text,
                    displayOrder = idx,
                )
            }
            return QuestionSnapshot(
                title = cmd.title,
                description = cmd.description,
                type = InputType.valueOrThrows(cmd.type),
                version = 0L,
                selectableOptions = selectableOptions,
            )
        }

        fun of(cmd: FormCommand.Question, newVersion: Long): QuestionSnapshot {
            val selectableOptions: List<SelectableOption> = cmd.selectableOptions.mapIndexed { idx, option ->
                SelectableOption.of(
                    value = option.text,
                    displayOrder = idx,
                )
            }
            return QuestionSnapshot(
                title = cmd.title,
                description = cmd.description,
                type = InputType.valueOrThrows(cmd.type),
                version = newVersion,
                selectableOptions = selectableOptions,
                questionTemplateId = cmd.questionTemplateId,
            )
        }
    }
}
