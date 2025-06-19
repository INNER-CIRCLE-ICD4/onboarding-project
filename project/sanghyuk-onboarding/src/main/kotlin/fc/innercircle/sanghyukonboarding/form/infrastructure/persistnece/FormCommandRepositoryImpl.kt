package fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece

import fc.innercircle.sanghyukonboarding.form.domain.model.Form
import fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa.FormJpaRepository
import fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa.QuestionSnapshotJpaRepository
import fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa.QuestionTemplateJpaRepository
import fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa.SelectableOptionJpaRepository
import fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa.entity.FormEntity
import fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa.entity.QuestionSnapshotEntity
import fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa.entity.QuestionTemplateEntity
import fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa.entity.SelectableOptionEntity
import fc.innercircle.sanghyukonboarding.form.service.port.FormCommandRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class FormCommandRepositoryImpl(
    private val formJpaRepository: FormJpaRepository,
    private val questionTemplateJpaRepository: QuestionTemplateJpaRepository,
    private val questionSnapshotJpaRepository: QuestionSnapshotJpaRepository,
    private val selectableOptionJpaRepository: SelectableOptionJpaRepository,

    ): FormCommandRepository {

    override fun insertOrUpdate(form: Form): String {
        // 참고: 진정한 의미의 "업데이트"를 구현하려면, 이 양식과 관련된 기존 하위 엔티티들을
        // 새로운 상태를 삽입하기 전에 삭제하는 등의 처리가 필요할 수 있습니다.
        // 현재 수정안은 보고된 예외를 수정하는 데 중점을 둡니다.

        // 1. Form 저장
        val formEntity = formJpaRepository.save(FormEntity.fromDomain(form))

        // 2. 자식 엔티티들을 계층적으로 저장하여 TransientPropertyValueException 문제 해결
        form.questionTemplates.list().forEach { template ->
            // 2-1. Template 저장
            // 먼저 부모 엔티티를 저장하여 영속 상태로 만듭니다.
            val templateEntity = QuestionTemplateEntity.from(template, formEntity)
            val persistedTemplateEntity = questionTemplateJpaRepository.save(templateEntity)

            template.snapshots.list().forEach { snapshot ->
                // 2-2. Snapshot 저장
                // 영속 상태의 부모 엔티티(persistedTemplateEntity)를 참조하여 자식 엔티티를 생성합니다.
                val snapshotEntity = QuestionSnapshotEntity.from(snapshot, persistedTemplateEntity)
                val persistedSnapshotEntity = questionSnapshotJpaRepository.save(snapshotEntity)

                // 2-3. Options는 한번에 저장 (Bulk save)
                if (snapshot.selectableOptions.list().isNotEmpty()) {
                    val optionEntities = snapshot.selectableOptions.list().map { option ->
                        SelectableOptionEntity.from(option, persistedSnapshotEntity)
                    }
                    selectableOptionJpaRepository.saveAll(optionEntities)
                }
            }
        }

        return formEntity.id
    }

    override fun deleteAll() {
        selectableOptionJpaRepository.deleteAll()
        questionSnapshotJpaRepository.deleteAll()
        questionTemplateJpaRepository.deleteAll()
        formJpaRepository.deleteAll()
    }
}
