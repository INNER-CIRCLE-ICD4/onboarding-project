package fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece

import fc.innercircle.sanghyukonboarding.common.domain.exception.CustomException
import fc.innercircle.sanghyukonboarding.common.domain.exception.ErrorCode
import fc.innercircle.sanghyukonboarding.form.domain.model.Form
import fc.innercircle.sanghyukonboarding.form.domain.model.QuestionSnapshot
import fc.innercircle.sanghyukonboarding.form.domain.model.QuestionTemplate
import fc.innercircle.sanghyukonboarding.form.domain.model.SelectableOption
import fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa.FormJpaRepository
import fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa.QuestionSnapshotJpaRepository
import fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa.QuestionTemplateJpaRepository
import fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa.SelectableOptionJpaRepository
import fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa.entity.QuestionSnapshotEntity
import fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa.entity.QuestionTemplateEntity
import fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa.entity.SelectableOptionEntity
import fc.innercircle.sanghyukonboarding.form.domain.service.port.FormQueryRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Component
class FormQueryRepositoryImpl(
    private val formJpaRepository: FormJpaRepository,
    private val questionTemplateJpaRepository: QuestionTemplateJpaRepository,
    private val questionSnapshotJpaRepository: QuestionSnapshotJpaRepository,
    private val selectableOptionJpaRepository: SelectableOptionJpaRepository,
): FormQueryRepository {

    override fun getById(id: String): Form {
        // 1. Form 조회
        val formEntity = formJpaRepository.findById(id)
            .orElseThrow { CustomException(ErrorCode.FORM_NOT_FOUND.withArgs(id)) }

        // 2. Template 엔티티 조회
        val templateEntities: Set<QuestionTemplateEntity> =
            questionTemplateJpaRepository
                .findAllByDeletedAndFormEntity(false, formEntity)
                .toSet()

        // 3. Snapshot 엔티티 조회
        val snapshotEntities: Set<QuestionSnapshotEntity> =
            questionSnapshotJpaRepository
                .findAllByQuestionTemplateEntityIn(templateEntities)
                .toSet()

        // 4. Option 엔티티 조회 및 도메인 변환
        val selectableOptions: List<SelectableOption> =
            selectableOptionJpaRepository
                .findAllByQuestionSnapshotEntityIn(snapshotEntities)
                .map(SelectableOptionEntity::toDomain)

        // 5. 스냅샷 → 도메인 변환
        val snapshots: List<QuestionSnapshot> = snapshotEntities.map { snapshot ->
            snapshot.toDomain(selectableOptions)
        }

        // 6. 템플릿 → 도메인 변환
        val templates: List<QuestionTemplate> = templateEntities.map { template ->
            template.toDomain(snapshots)
        }

        // 7. 폼 → 도메인 변환 및 반환
        return formEntity.toDomain(templates)
    }
}
