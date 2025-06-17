package fc.innercircle.sanghyukonboarding.form.domain.model.vo

import fc.innercircle.sanghyukonboarding.common.domain.exception.CustomException
import fc.innercircle.sanghyukonboarding.common.domain.exception.ErrorCode
import fc.innercircle.sanghyukonboarding.form.domain.model.QuestionSnapshot

data class QuestionSnapshots(
    private val values: List<QuestionSnapshot> = emptyList()
) {

    init {
        values.sortedByDescending { it.version }
    }

    fun list(): List<QuestionSnapshot> {
        return values
    }

    fun filter(predicate: (QuestionSnapshot) -> Boolean): QuestionSnapshots {
        return QuestionSnapshots(
            values = values.filter(predicate)
        )
    }

    fun getByVersion(version: Long): QuestionSnapshot {
        return values.firstOrNull { it.version == version }
            ?: throw CustomException(ErrorCode.QUESTION_VERSION_NOT_FOUND.withArgs(version))
    }

    fun add(newSnapshot: QuestionSnapshot): QuestionSnapshots {
        isDuplicateOrThrows(newSnapshot)
        return QuestionSnapshots(
            values = values + newSnapshot
        )
    }

    private fun isDuplicateOrThrows(newSnapshot: QuestionSnapshot) {
        if (values.any { it.version == newSnapshot.version }) {
            throw CustomException(ErrorCode.DUPLICATE_QUESTION_SNAPSHOT_VERSION.withArgs(newSnapshot.version))
        }
    }
}
