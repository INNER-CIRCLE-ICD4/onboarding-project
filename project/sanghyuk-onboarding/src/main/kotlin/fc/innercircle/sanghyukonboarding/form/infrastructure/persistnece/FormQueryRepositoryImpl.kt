package fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece

import fc.innercircle.sanghyukonboarding.common.domain.exception.CustomException
import fc.innercircle.sanghyukonboarding.common.domain.exception.ErrorCode
import fc.innercircle.sanghyukonboarding.form.domain.model.Form
import fc.innercircle.sanghyukonboarding.form.domain.model.Question
import fc.innercircle.sanghyukonboarding.form.domain.service.port.FormQueryRepository
import fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa.FormJpaRepository
import fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa.QuestionJpaRepository
import fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa.entity.QuestionEntity
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Component
class FormQueryRepositoryImpl(
    private val formJpaRepository: FormJpaRepository,
    private val questionJpaRepository: QuestionJpaRepository,
) : FormQueryRepository {

    override fun getById(id: String): Form {
        val formEntity = formJpaRepository.findById(id)
            .orElseThrow { CustomException(ErrorCode.FORM_NOT_FOUND.withArgs(id)) }
        val questionEntities: Set<QuestionEntity> =
            questionJpaRepository
                .findAllByDeletedAndFormEntity(false, formEntity)
                .toSet()

        // 6. 템플릿 → 도메인 변환
        val questions: List<Question> = questionEntities.map { questionEntity ->
            questionEntity.toDomain()
        }

        // 7. 폼 → 도메인 변환 및 반환
        return formEntity.toDomain(questions)
    }
}
