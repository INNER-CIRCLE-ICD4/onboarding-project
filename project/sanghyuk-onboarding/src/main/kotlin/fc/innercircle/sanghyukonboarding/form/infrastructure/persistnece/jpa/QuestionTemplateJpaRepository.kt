package fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa

import fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa.entity.FormEntity
import fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa.entity.QuestionTemplateEntity
import org.springframework.data.jpa.repository.JpaRepository

interface QuestionTemplateJpaRepository : JpaRepository<QuestionTemplateEntity, String> {
    fun findAllByFormEntity(formEntity: FormEntity): List<QuestionTemplateEntity>
}
