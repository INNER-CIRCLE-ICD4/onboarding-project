package fc.innercircle.sanghyukonboarding.formreply.infrastructure.persistence

import fc.innercircle.sanghyukonboarding.common.domain.exception.CustomException
import fc.innercircle.sanghyukonboarding.common.domain.exception.ErrorCode
import fc.innercircle.sanghyukonboarding.formreply.domain.model.Answer
import fc.innercircle.sanghyukonboarding.formreply.domain.model.FormReply
import fc.innercircle.sanghyukonboarding.formreply.infrastructure.persistence.jpa.AnswerJpaRepository
import fc.innercircle.sanghyukonboarding.formreply.infrastructure.persistence.jpa.FormReplyJpaRepository
import fc.innercircle.sanghyukonboarding.formreply.infrastructure.persistence.jpa.entity.FormReplyEntity
import fc.innercircle.sanghyukonboarding.formreply.application.port.FormReplyReader
import org.springframework.stereotype.Component

@Component
class FormReplyReaderImpl(
    private val formReplyJpaRepository: FormReplyJpaRepository,
    private val answerJpaRepository: AnswerJpaRepository
): FormReplyReader {

    override fun getById(formReplyId: String): FormReply {
        // 1. FormReply 엔티티 조회
        val formReplyEntity: FormReplyEntity = formReplyJpaRepository.findById(formReplyId)
            .orElseThrow {
                CustomException(ErrorCode.FORM_REPLY_NOT_FOUND.withArgs(formReplyId))
            }

        // 2. Answer 엔티티 조회
        val answers: List<Answer> = answerJpaRepository.findAllByFormReplyEntity(formReplyEntity)
            .map { entity ->
                entity.toDomain()
            }

        // 3. FormReply 도메인 모델로 변환
        return formReplyEntity.toDomain(answers)
    }
}
