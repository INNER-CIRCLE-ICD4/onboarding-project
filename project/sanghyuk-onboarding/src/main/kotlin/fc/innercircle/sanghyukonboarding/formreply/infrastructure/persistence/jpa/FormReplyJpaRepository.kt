package fc.innercircle.sanghyukonboarding.formreply.infrastructure.persistence.jpa

import fc.innercircle.sanghyukonboarding.formreply.infrastructure.persistence.jpa.entity.FormReplyEntity
import org.springframework.data.jpa.repository.JpaRepository

interface FormReplyJpaRepository : JpaRepository<FormReplyEntity, String> {
    fun findAllByFormId(formId: String): List<FormReplyEntity>
}
