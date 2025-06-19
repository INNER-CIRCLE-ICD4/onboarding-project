package fc.innercircle.sanghyukonboarding.formreply.infrastructure.persistence

import fc.innercircle.sanghyukonboarding.formreply.domain.model.Answer
import fc.innercircle.sanghyukonboarding.formreply.domain.model.FormReply
import fc.innercircle.sanghyukonboarding.formreply.domain.service.port.FormReplyReader
import fc.innercircle.sanghyukonboarding.formreply.infrastructure.persistence.jpa.AnswerJpaRepository
import fc.innercircle.sanghyukonboarding.formreply.infrastructure.persistence.jpa.FormReplyJpaRepository
import fc.innercircle.sanghyukonboarding.formreply.infrastructure.persistence.jpa.entity.FormReplyEntity
import org.springframework.stereotype.Component

@Component
class FormReplyReaderImpl(
    private val formReplyJpaRepository: FormReplyJpaRepository,
    private val answerJpaRepository: AnswerJpaRepository
): FormReplyReader {

    override fun getAllByFormId(formId: String): List<FormReply> {
        // 1. FormReply 엔티티 리스트 조회
        val formReplyEntities: List<FormReplyEntity> = formReplyJpaRepository.findAllByFormId(formId)

        // 2. 각 FormReply 엔티티에 대해 Answer 엔티티 조회 및 도메인 모델로 변환
        return formReplyEntities.map { formReplyEntity ->
            val answers: List<Answer> = answerJpaRepository.findAllByFormReplyEntity(formReplyEntity)
                .map { entity -> entity.toDomain() }
            formReplyEntity.toDomain(answers)
        }
    }
}
