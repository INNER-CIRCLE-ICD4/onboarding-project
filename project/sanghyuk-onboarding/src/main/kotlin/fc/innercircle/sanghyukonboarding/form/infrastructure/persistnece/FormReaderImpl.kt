package fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece

import fc.innercircle.sanghyukonboarding.form.domain.model.Form
import fc.innercircle.sanghyukonboarding.form.domain.model.QuestionSnapshot
import fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa.FormJpaRepository
import fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa.QuestionSnapshotJpaRepository
import fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa.QuestionTemplateJpaRepository
import fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa.SelectableOptionJpaRepository
import fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa.entity.SelectableOptionEntity
import fc.innercircle.sanghyukonboarding.form.service.port.FormReader
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Component
class FormReaderImpl(
    private val formJpaRepository: FormJpaRepository,
    private val questionTemplateJpaRepository: QuestionTemplateJpaRepository,
    private val questionSnapshotJpaRepository: QuestionSnapshotJpaRepository,
    private val selectableOptionJpaRepository: SelectableOptionJpaRepository,
): FormReader {
    
    /**
     * 폼 ID로 폼을 조회합니다.
     */
    override fun findById(id: String): Form? =
        formJpaRepository.findById(id)
            .map { formEntity ->
                // 1) 템플릿 조회
                val templateEntities = questionTemplateJpaRepository.findAllByFormEntity(formEntity).toSet()

                // 2) 스냅샷 조회
                val snapshotEntities =
                    questionSnapshotJpaRepository.findAllByQuestionTemplateEntityIn(templateEntities).toSet()

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
