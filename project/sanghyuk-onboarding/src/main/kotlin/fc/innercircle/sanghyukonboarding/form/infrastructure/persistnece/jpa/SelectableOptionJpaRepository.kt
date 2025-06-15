package fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa

import fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa.entity.QuestionSnapshotEntity
import fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa.entity.SelectableOptionEntity
import org.springframework.data.jpa.repository.JpaRepository

interface SelectableOptionJpaRepository : JpaRepository<SelectableOptionEntity, String> {
    fun findAllByQuestionSnapshotEntityIn(entities: Set<QuestionSnapshotEntity>): List<SelectableOptionEntity>
}
