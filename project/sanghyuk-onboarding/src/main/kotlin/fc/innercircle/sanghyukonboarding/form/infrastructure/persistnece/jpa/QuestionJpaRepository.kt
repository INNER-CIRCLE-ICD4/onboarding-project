package fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa

import fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa.entity.FormEntity
import fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa.entity.QuestionEntity
import org.springframework.data.jpa.repository.JpaRepository

interface QuestionJpaRepository : JpaRepository<QuestionEntity, String> {
    fun findAllByDeletedAndFormEntity(deleted: Boolean, formEntity: FormEntity): List<QuestionEntity>
}
