package fc.innercircle.sanghyukonboarding.form.domain.model

import fc.innercircle.sanghyukonboarding.common.domain.exception.CustomException
import fc.innercircle.sanghyukonboarding.common.domain.exception.ErrorCode
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
        values = selectableOptions.filter { it ->
            it.isNew() || it.isSelectableOptionOf(this)
        }
    )

    init {
        verifyInputType(selectableOptions)
        validateRequiredFields()
    }

    /**
     * 비즈니스 정책 검사
     * 1. 질문의 타입이 텍스트 타입인 경우 선택 가능한 옵션이 없어야 한다.
     * 2. 질문의 타입이 선택 가능한 타입인 경우 선택 가능한 옵션이 있어야 한다.
     */
    private fun verifyInputType(selectableOptions: List<SelectableOption>) {
        when {
            type.isTextType() && selectableOptions.isNotEmpty() -> {
                throw CustomException(ErrorCode.TEXT_TYPE_INPUT_ERROR.withArgs(title))
            }
            type.isSelectableType() && selectableOptions.isEmpty() -> {
                throw CustomException(ErrorCode.SELECTABLE_TYPE_INPUT_ERROR.withArgs(title))
            }
        }
    }

    fun isNew(): Boolean {
        return id.isBlank()
    }

    fun isSnapshotOf(template: QuestionTemplate): Boolean {
        return this.questionTemplateId == template.id
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
        val newSnapshot: QuestionSnapshot = of(
            id = this.id,
            cmd = cmd,
            version = this.version
        )
        return this != newSnapshot
    }

    companion object {
        fun of(
            id: String = "",
            cmd: FormCommand.Question,
            version: Long = 0L,
        ): QuestionSnapshot {
            val selectableOptions: List<SelectableOption> = cmd.selectableOptions.mapIndexed { idx, optionCmd ->
                SelectableOption.of(
                    id = optionCmd.selectableOptionId,
                    text = optionCmd.text,
                    displayOrder = idx,
                    questionSnapshotId = id
                )
            }
            return QuestionSnapshot(
                id = id,
                title = cmd.title,
                description = cmd.description,
                type = InputType.valueOrThrows(cmd.type),
                version = version,
                selectableOptions = selectableOptions,
                questionTemplateId = cmd.questionTemplateId,
            )
        }
    }
}
