package fc.innercircle.sanghyukonboarding.form.domain.model

import fc.innercircle.sanghyukonboarding.form.domain.dto.command.FormCommand
import fc.innercircle.sanghyukonboarding.form.domain.model.vo.QuestionSnapshots
import fc.innercircle.sanghyukonboarding.form.domain.validator.QuestionTemplateValidator

open class QuestionTemplate(
    val id: String = "",
    val version: Long,
    val required: Boolean,
    val displayOrder: Int,
    snapshots: List<QuestionSnapshot>,
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

    companion object {
        fun of(cmd: FormCommand.Question, displayOrder: Int): QuestionTemplate {
            val questionSnapshots: List<QuestionSnapshot> = listOf(
                QuestionSnapshot.from(cmd)
            )
            return QuestionTemplate(
                version = cmd.version,
                required = cmd.required,
                displayOrder = displayOrder,
                snapshots = questionSnapshots,
            )
        }
    }
}
