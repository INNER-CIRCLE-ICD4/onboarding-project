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

    companion object {
        fun from(cmd: FormCommand.Question): QuestionSnapshot {
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
                version = cmd.version,
                selectableOptions = selectableOptions,
            )
        }
    }
}
