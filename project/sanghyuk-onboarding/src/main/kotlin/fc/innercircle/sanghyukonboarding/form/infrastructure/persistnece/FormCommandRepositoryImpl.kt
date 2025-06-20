package fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece

import fc.innercircle.sanghyukonboarding.form.domain.model.Form
import fc.innercircle.sanghyukonboarding.form.domain.service.port.FormCommandRepository
import fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa.FormJpaRepository
import fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa.QuestionJpaRepository
import fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa.entity.FormEntity
import fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa.entity.QuestionEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class FormCommandRepositoryImpl(
    private val formJpaRepository: FormJpaRepository,
    private val questionJpaRepository: QuestionJpaRepository,

    ): FormCommandRepository {

    override fun insertOrUpdate(form: Form): String {
        val formEntity = formJpaRepository.save(FormEntity.fromDomain(form))
        form.questions.list().forEach { template ->
            val question = QuestionEntity.from(template, formEntity)
            questionJpaRepository.save(question)
        }
        return formEntity.id
    }

    override fun deleteAll() {
        questionJpaRepository.deleteAll()
        formJpaRepository.deleteAll()
    }
}
