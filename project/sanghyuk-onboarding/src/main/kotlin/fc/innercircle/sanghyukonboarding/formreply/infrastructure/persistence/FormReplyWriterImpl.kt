package fc.innercircle.sanghyukonboarding.formreply.infrastructure.persistence

import fc.innercircle.sanghyukonboarding.formreply.domain.model.FormReply
import fc.innercircle.sanghyukonboarding.formreply.infrastructure.persistence.jpa.AnswerJpaRepository
import fc.innercircle.sanghyukonboarding.formreply.infrastructure.persistence.jpa.FormReplyJpaRepository
import fc.innercircle.sanghyukonboarding.formreply.infrastructure.persistence.jpa.entity.AnswerEntity
import fc.innercircle.sanghyukonboarding.formreply.infrastructure.persistence.jpa.entity.FormReplyEntity
import fc.innercircle.sanghyukonboarding.formreply.application.port.FormReplyWriter
import org.springframework.stereotype.Component

@Component
class FormReplyWriterImpl(
    private val formReplyJpaRepository: FormReplyJpaRepository,
    private val answerJpaRepository: AnswerJpaRepository
): FormReplyWriter {
    override fun insertOrUpdate(formReply: FormReply): String {
        val formReplyEntity = FormReplyEntity.fromDomain(formReply)
        formReplyJpaRepository.save(formReplyEntity)

        val answerEntities = mutableListOf<AnswerEntity>();

        formReply.answers.list().forEach { answer ->
            answerEntities.add(AnswerEntity.fromDomain(answer, formReplyEntity))
        }

        answerJpaRepository.saveAll(answerEntities)
        return formReplyEntity.id
    }
}
