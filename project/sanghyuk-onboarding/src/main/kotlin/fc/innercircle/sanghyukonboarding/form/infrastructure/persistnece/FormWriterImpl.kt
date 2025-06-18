package fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece

import fc.innercircle.sanghyukonboarding.common.domain.model.IdGenerator
import fc.innercircle.sanghyukonboarding.form.domain.model.Form
import fc.innercircle.sanghyukonboarding.form.domain.model.QuestionSnapshot
import fc.innercircle.sanghyukonboarding.form.domain.model.QuestionTemplate
import fc.innercircle.sanghyukonboarding.form.domain.model.SelectableOption
import fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa.FormJpaRepository
import fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa.entity.FormEntity
import fc.innercircle.sanghyukonboarding.form.service.port.FormWriter
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class FormWriterImpl(
    private val formJpaRepository: FormJpaRepository,
    private val jdbcTemplate: JdbcTemplate,
): FormWriter {

    private fun saveForm(form: Form): FormEntity {
        return formJpaRepository.save(FormEntity.fromDomain(form))
    }

    override fun insertOrUpdate(form: Form): String {
        // 1) Form 저장 (insert or update)
        val formEntity = saveForm(form)
        val formId = formEntity.id

        // 2) QuestionTemplate 배치 insert/update
        val templates = form.questionTemplates.list()
        saveTemplatesBatch(templates, formId)

        // 3) QuestionSnapshot 배치 insert/update
        val snapshots = templates.flatMap { it.snapshots.list() }
        saveSnapshotsBatch(snapshots)

        // 4) SelectableOption 배치 insert/update
        val options = snapshots.flatMap { it.selectableOptions.list() }
        saveOptionsBatch(options)

        return formId
    }

    override fun deleteAll() {
        formJpaRepository.deleteAll()
    }

    // --- Batch helper methods ------------------------------------------------
    private fun saveTemplatesBatch(templates: List<QuestionTemplate>, formId: String) {
        if (templates.isEmpty()) return
        val (inserts, updates) = templates.partition { it.id.isBlank() }

        if (inserts.isNotEmpty()) {
            val newIds = generateIds(inserts.size)
            jdbcTemplate.batchUpdate(
                """
                INSERT INTO question_template(id, version, required, display_order, deleted, form_id)
                VALUES (?, ?, ?, ?, ?, ?)
                """.trimIndent(),
                inserts.mapIndexed { idx, tpl ->
                    arrayOf(
                        newIds[idx],
                        tpl.version,
                        tpl.required,
                        tpl.displayOrder,
                        tpl.deleted,
                        formId
                    )
                }
            )
        }

        if (updates.isNotEmpty()) {
            jdbcTemplate.batchUpdate(
                """
                UPDATE question_template
                SET version = ?, required = ?, display_order = ?, deleted = ?
                WHERE id = ?
                """.trimIndent(),
                updates.map { tpl ->
                    arrayOf(
                        tpl.version,
                        tpl.required,
                        tpl.displayOrder,
                        tpl.deleted,
                        tpl.id
                    )
                }
            )
        }
    }

    private fun saveSnapshotsBatch(snapshots: List<QuestionSnapshot>) {
        if (snapshots.isEmpty()) return
        val (inserts, updates) = snapshots.partition { it.id.isBlank() }

        if (inserts.isNotEmpty()) {
            val newIds = generateIds(inserts.size)
            jdbcTemplate.batchUpdate(
                """
                INSERT INTO question_snapshot(
                  id, title, description, type, version, question_template_id
                ) VALUES (?, ?, ?, ?, ?, ?)
                """.trimIndent(),
                inserts.mapIndexed { idx, snap ->
                    arrayOf(
                        newIds[idx],
                        snap.title,
                        snap.description,
                        snap.type.name,
                        snap.version,
                        snap.questionTemplateId
                    )
                }
            )
        }

        if (updates.isNotEmpty()) {
            jdbcTemplate.batchUpdate(
                """
                UPDATE question_snapshot
                SET title = ?, description = ?, type = ?, version = ?
                WHERE id = ?
                """.trimIndent(),
                updates.map { snap ->
                    arrayOf(
                        snap.title,
                        snap.description,
                        snap.type.name,
                        snap.version,
                        snap.id
                    )
                }
            )
        }
    }

    private fun saveOptionsBatch(options: List<SelectableOption>) {
        if (options.isEmpty()) return
        val (inserts, updates) = options.partition { it.id.isBlank() }

        if (inserts.isNotEmpty()) {
            val newIds = generateIds(inserts.size)
            jdbcTemplate.batchUpdate(
                """
                INSERT INTO selectable_option(id, text, question_snapshot_id)
                VALUES (?, ?, ?)
                """.trimIndent(),
                inserts.mapIndexed { idx, opt ->
                    arrayOf(
                        newIds[idx],
                        opt.text,
                        opt.questionSnapshotId
                    )
                }
            )
        }

        if (updates.isNotEmpty()) {
            jdbcTemplate.batchUpdate(
                """
                UPDATE selectable_option
                SET text = ?
                WHERE id = ?
                """.trimIndent(),
                updates.map { opt ->
                    arrayOf(
                        opt.text,
                        opt.id
                    )
                }
            )
        }
    }

    private fun generateIds(count: Int): List<String> =
        List(count) { IdGenerator.next() }

}
