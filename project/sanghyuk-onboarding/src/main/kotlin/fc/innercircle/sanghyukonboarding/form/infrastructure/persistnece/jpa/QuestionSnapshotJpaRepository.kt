package fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa

import fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa.entity.QuestionSnapshotEntity
import fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa.entity.QuestionTemplateEntity
import org.springframework.data.jpa.repository.JpaRepository

interface QuestionSnapshotJpaRepository : JpaRepository<QuestionSnapshotEntity, String> {
    fun findAllByQuestionTemplateEntityIn(questionTemplateEntities: Set<QuestionTemplateEntity>): List<QuestionSnapshotEntity>
}
