package fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa.entity

import fc.innercircle.sanghyukonboarding.common.domain.model.IdGenerator
import fc.innercircle.sanghyukonboarding.form.domain.model.QuestionSnapshot
import fc.innercircle.sanghyukonboarding.form.domain.model.QuestionTemplate
import fc.innercircle.sanghyukonboarding.form.domain.validator.QuestionTemplateValidator
import jakarta.persistence.Column
import jakarta.persistence.ConstraintMode
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.PrePersist
import jakarta.persistence.Table
import org.springframework.data.domain.Persistable

@Table(name = "question_template",
    indexes = [],
    uniqueConstraints = [])
@Entity(name = "question_template")
open class QuestionTemplateEntity(
    @Id
    @Column(
        name = "id",
        nullable = false,
        updatable = false,
        unique = true,
        columnDefinition = "char(26) not null comment 'ID'"
    )
    private var id: String? = null,
    @Column(nullable = false, columnDefinition = "bigint default 0 not null comment '버전'")
    val version: Long,
    @Column(nullable = false, columnDefinition = "boolean default false comment '필수 여부'")
    val required: Boolean,
    @Column(nullable = false, columnDefinition = "int default 0 comment '항목 순서'")
    val displayOrder: Int,
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "form_id",
        columnDefinition = "char(26) not null comment '설문 ID'",
        referencedColumnName = "id",
        nullable = false,
        foreignKey = ForeignKey(name = "fk_question_template_form", value = ConstraintMode.NO_CONSTRAINT)
    )
    val formEntity: FormEntity,
): Persistable<String> {

    init {
        validateRequiredFields()
    }

    private fun validateRequiredFields() {
        QuestionTemplateValidator.validateVersion(version)
        QuestionTemplateValidator.validateDisplayOrder(displayOrder)
    }

    fun toDomain(questionSnapshots: List<QuestionSnapshot>): QuestionTemplate {
        return QuestionTemplate(
            id = id!!,
            version = version,
            required = required,
            displayOrder = displayOrder,
            snapshots = questionSnapshots,
            formId = formEntity.id!!,
        )
    }

    override fun getId(): String? {
        return id
    }

    override fun isNew(): Boolean {
        return id == null
    }

    @PrePersist
    fun prePersist() {
        if (id == null) {
            id = IdGenerator.next()
        }
    }

    companion object {
        fun from(questionTemplate: QuestionTemplate, formEntity: FormEntity): QuestionTemplateEntity {
            return QuestionTemplateEntity(
                version = 0,
                required = questionTemplate.required,
                displayOrder = questionTemplate.displayOrder,
                formEntity = formEntity,
            )
        }
    }
}
