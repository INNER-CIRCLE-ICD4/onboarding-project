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
import fc.innercircle.sanghyukonboarding.form.service.port.FormWriter
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class FormWriterImpl(
    private val formJpaRepository: FormJpaRepository,
    private val questionTemplateJpaRepository: QuestionTemplateJpaRepository,
    private val questionSnapshotJpaRepository: QuestionSnapshotJpaRepository,
    private val selectableOptionJpaRepository: SelectableOptionJpaRepository,

    ): FormWriter {

    override fun insertOrUpdate(form: Form): String {

        // 1. Form 저장
        val formEntity = FormEntity.fromDomain(form)
        formJpaRepository.save(formEntity)

        // 2. 엔티티 컬렉션을 담을 가변 리스트 준비
        val templateEntities = mutableListOf<QuestionTemplateEntity>()
        val snapshotEntities = mutableListOf<QuestionSnapshotEntity>()
        val optionEntities   = mutableListOf<SelectableOptionEntity>()

        // 3. 한 번만 questionTemplates.list() 호출
        form.questionTemplates.list().forEach { template ->
            // 3-1. Template 엔티티 변환 및 추가
            val templateEntity = QuestionTemplateEntity.from(template, formEntity)
            templateEntities.add(templateEntity)

            // 3-2. Snapshot 엔티티 변환 및 추가
            template.snapshots.list().forEach { snapshot ->
                val snapshotEntity = QuestionSnapshotEntity.from(snapshot, templateEntity)
                snapshotEntities.add(snapshotEntity)

                // 3-3. Option 엔티티 변환 및 추가
                snapshot.selectableOptions.list().forEach { option ->
                    optionEntities.add(SelectableOptionEntity.from(option, snapshotEntity))
                }
            }
        }

        // 4. 각 레벨별 bulk save
        questionTemplateJpaRepository.saveAll(templateEntities)
        questionSnapshotJpaRepository.saveAll(snapshotEntities)
        selectableOptionJpaRepository.saveAll(optionEntities)

        return formEntity.id
    }

    override fun deleteAll() {
        selectableOptionJpaRepository.deleteAll()
        questionSnapshotJpaRepository.deleteAll()
        questionTemplateJpaRepository.deleteAll()
        formJpaRepository.deleteAll()
    }
}
