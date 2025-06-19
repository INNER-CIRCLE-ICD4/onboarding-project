package fc.innercircle.sanghyukonboarding.formreply.infrastructure.persistence.jpa

import fc.innercircle.sanghyukonboarding.formreply.infrastructure.persistence.jpa.entity.AnswerEntity
import fc.innercircle.sanghyukonboarding.formreply.infrastructure.persistence.jpa.entity.FormReplyEntity
import org.springframework.data.jpa.repository.JpaRepository

interface AnswerJpaRepository: JpaRepository<AnswerEntity, String> {
    fun findAllByFormReplyEntity(formReplyEntity: FormReplyEntity): List<AnswerEntity>
}
