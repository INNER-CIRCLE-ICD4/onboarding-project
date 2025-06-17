package fc.innercircle.sanghyukonboarding.form.domain.model

import fc.innercircle.sanghyukonboarding.form.domain.dto.command.FormCreateCommand
import fc.innercircle.sanghyukonboarding.form.domain.dto.command.FormUpdateCommand
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

    fun isModified(cmd: FormUpdateCommand.Question): Boolean {
        return title != cmd.title ||
                description != cmd.description ||
                type != InputType.valueOrThrows(cmd.type) ||
                selectableOptions.list().size != cmd.selectableOptions.size ||
                selectableOptions.list().mapIndexed { idx, option ->
                    option.text != cmd.selectableOptions[idx].text ||
                    option.displayOrder != idx
                }.any { it }
    }

    companion object {
        fun of(cmd: FormCreateCommand.Question): QuestionSnapshot {
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

        fun of(cmd: FormUpdateCommand.Question, newVersion: Long): QuestionSnapshot {
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
