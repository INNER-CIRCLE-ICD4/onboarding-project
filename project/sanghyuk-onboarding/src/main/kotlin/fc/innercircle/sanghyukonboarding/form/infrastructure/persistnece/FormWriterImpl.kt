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

    override fun insertOrUpdate(form: Form): String {
        val formEntity = saveForm(form)
        val formId = formEntity.id

        val templates = form.questionTemplates.list()
        val templateIds = generateIds(templates.size)
        saveTemplatesBatch(templates, templateIds, formId)

        val snapshots = templates.flatMap { it.snapshots.list() }
        val snapshotIds = generateIds(snapshots.size)
        saveSnapshotsBatch(templates, templateIds, snapshots, snapshotIds)

        val options = snapshots.flatMap { it.selectableOptions.list() }
        val optionIds = generateIds(options.size)
        saveOptionsBatch(snapshots, snapshotIds, options, optionIds)

        // 모든 엔티티 저장 후, ID를 반환
        return formId
    }

    override fun deleteAll() {
        // DB의 모든 항목을 초기화
        formJpaRepository.deleteAll()
    }

    private fun saveForm(form: Form): FormEntity {
        return formJpaRepository.save(FormEntity.fromDomain(form))
    }

    private fun saveTemplatesBatch(
        templates: List<QuestionTemplate>,
        templateIds: List<String>,
        formId: String
    ) {
        if (templates.isEmpty()) return
        val sql = """
            INSERT INTO question_template
            (id, version, required, display_order, form_id)
            VALUES (?, 0, ?, ?, ?)
        """.trimIndent()

        val args = templates.zip(templateIds).map { (t, id) ->
            arrayOf<Any>(id, t.required, t.displayOrder, formId)
        }

        jdbcTemplate.batchUpdate(sql, args)
    }

    private fun saveSnapshotsBatch(
        templates: List<QuestionTemplate>,
        templateIds: List<String>,
        snapshots: List<QuestionSnapshot>,
        snapshotIds: List<String>
    ) {
        if (snapshots.isEmpty()) return
        val sql = """
            INSERT INTO question_snapshot
            (id, title, description, type, version, question_template_id)
            VALUES (?, ?, ?, ?, 0, ?)
        """.trimIndent()

        val args = mutableListOf<Array<Any>>()
        var idx = 0
        templates.forEachIndexed { ti, template ->
            val parentId = templateIds[ti]
            template.snapshots.list().forEach { snap ->
                args += arrayOf<Any>(
                    snapshotIds[idx],
                    snap.title,
                    snap.description,
                    snap.type.name,
                    parentId
                )
                idx++
            }
        }

        jdbcTemplate.batchUpdate(sql, args)
    }

    private fun saveOptionsBatch(
        snapshots: List<QuestionSnapshot>,
        snapshotIds: List<String>,
        options: List<SelectableOption>,
        optionIds: List<String>
    ) {
        if (options.isEmpty()) return
        val sql = """
            INSERT INTO selectable_option
            (id, text, display_order, question_snapshot_id)
            VALUES (?, ?, ?, ?)
        """.trimIndent()

        val args = mutableListOf<Array<Any>>()
        var idx = 0
        snapshots.forEachIndexed { si, snap ->
            snap.selectableOptions.list().forEach { opt ->
                args += arrayOf<Any>(
                    optionIds[idx],
                    opt.text,
                    opt.displayOrder,
                    snapshotIds[si]
                )
                idx++
            }
        }

        jdbcTemplate.batchUpdate(sql, args)
    }

    private fun generateIds(count: Int): List<String> = List(count) { IdGenerator.next() }
}
