package fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece

import fc.innercircle.sanghyukonboarding.common.domain.exception.CustomException
import fc.innercircle.sanghyukonboarding.common.domain.exception.ErrorCode
import fc.innercircle.sanghyukonboarding.form.domain.model.Form
import fc.innercircle.sanghyukonboarding.form.domain.model.QuestionSnapshot
import fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa.FormJpaRepository
import fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa.QuestionSnapshotJpaRepository
import fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa.QuestionTemplateJpaRepository
import fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa.SelectableOptionJpaRepository
import fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa.entity.FormEntity
import fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa.entity.QuestionSnapshotEntity
import fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa.entity.QuestionTemplateEntity
import fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa.entity.SelectableOptionEntity
import fc.innercircle.sanghyukonboarding.form.service.port.FormRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class FormRepositoryImpl(
    private val formJpaRepository: FormJpaRepository,
    private val questionTemplateJpaRepository: QuestionTemplateJpaRepository,
    private val questionSnapshotJpaRepository: QuestionSnapshotJpaRepository,
    private val selectableOptionJpaRepository: SelectableOptionJpaRepository,
) : FormRepository {

    @Transactional
    override fun insertOrUpdate(form: Form): Form {
        // 1. 폼 엔티티 저장
        val formEntity = formJpaRepository.save(FormEntity.fromDomain(form))

        // 2. 질문 템플릿 엔티티 변환 & 저장
        val templates = form.questionTemplates.list()
        val templateEntities = templates
            .map { QuestionTemplateEntity.from(it, formEntity) }
            .let(questionTemplateJpaRepository::saveAll)

        // 3. 질문 스냅샷 엔티티 변환 & 저장 (도메인 템플릿 ↔ 엔티티 템플릿을 zip 으로 묶음)
        val snapshotEntities = templates
            .zip(templateEntities)
            .flatMap { (template, templateEntity) ->
                template.snapshots.list().map { snapshot ->
                    QuestionSnapshotEntity.from(snapshot, templateEntity)
                }
            }
            .let(questionSnapshotJpaRepository::saveAll)

        // 4. 선택 가능한 옵션 엔티티 변환 & 저장 (도메인 스냅샷 ↔ 엔티티 스냅샷을 zip 으로 묶음)
        form.questionTemplates.list()
            .flatMap { it.snapshots.list() }
            .zip(snapshotEntities)
            .flatMap { (snapshot, snapshotEntity) ->
                snapshot.selectableOptions.list().map { option ->
                    SelectableOptionEntity.from(option, snapshotEntity)
                }
            }
            .let(selectableOptionJpaRepository::saveAll)

        return findById(formEntity.id!!)
            ?: throw CustomException(ErrorCode.FORM_NOT_FOUND.withArgs(formEntity.id))
    }

    /**
     * 폼 ID로 폼을 조회합니다.
     */
    override fun findById(id: String): Form? =
        formJpaRepository.findById(id)
            .map { formEntity ->
                // 1) 템플릿 조회
                val templateEntities = questionTemplateJpaRepository.findAllByFormEntity(formEntity).toSet()

                // 2) 스냅샷 조회
                val snapshotEntities = questionSnapshotJpaRepository.findAllByQuestionTemplateEntityIn(templateEntities).toSet()

                // 3) 옵션 조회
                val optionEntities = selectableOptionJpaRepository.findAllByQuestionSnapshotEntityIn(snapshotEntities)
                val selectableOptions = optionEntities.map(SelectableOptionEntity::toDomain)

                // 4) 도메인 변환: 스냅샷
                val snapshots: List<QuestionSnapshot> = snapshotEntities.map { snapshot ->
                    snapshot.toDomain(selectableOptions.filter { it.questionSnapshotId == snapshot.id })
                }

                // 4) 도메인 변환: 템플릿
                val templates = templateEntities.map { templateEntity ->
                    templateEntity.toDomain(snapshots.filter { snapshot ->
                        snapshot.questionTemplateId == templateEntity.id
                    })
                }

                // 6) 도메인 변환: 폼
                formEntity.toDomain(templates)
            }
            .orElse(null)
}
