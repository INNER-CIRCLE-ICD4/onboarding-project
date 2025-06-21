package fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa.entity

import fc.innercircle.sanghyukonboarding.common.domain.model.IdGenerator
import fc.innercircle.sanghyukonboarding.form.domain.model.Question
import fc.innercircle.sanghyukonboarding.form.domain.model.validator.QuestionTemplateValidator
import fc.innercircle.sanghyukonboarding.form.domain.model.vo.InputType
import fc.innercircle.sanghyukonboarding.formreply.infrastructure.persistence.jpa.entity.converter.ListToJsonConverter
import jakarta.persistence.Column
import jakarta.persistence.ConstraintMode
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.PrePersist
import jakarta.persistence.Table
import org.springframework.data.domain.Persistable

@Table(
    name = "question",
    indexes = [],
    uniqueConstraints = []
)
@Entity(name = "question")
open class QuestionEntity(
    @Id
    @Column(
        name = "id",
        nullable = false,
        updatable = false,
        unique = true,
        columnDefinition = "char(26) not null comment 'ID'"
    )
    private var id: String,
    @Column(nullable = false, columnDefinition = "bigint default 0 not null comment '버전'")
    val version: Long,
    @Column(nullable = false, columnDefinition = "boolean default false comment '필수 여부'")
    val required: Boolean,
    @Column(nullable = false, columnDefinition = "varchar(20) not null comment '입력 타입'")
    val type: String,
    @Column(nullable = false, columnDefinition = "int default 0 comment '항목 순서'")
    val displayOrder: Int,
    @Column(
        name = "title",
        nullable = false,
        columnDefinition = "varchar(100) not null comment '항목 제목'"
    )
    val title: String,
    @Column(
        name = "description",
        nullable = false,
        columnDefinition = "varchar(255) default '' not null comment '항목 설명'"
    )
    val description: String,
    @Column(
        name = "deleted",
        nullable = false,
        columnDefinition = "boolean default false not null comment '삭제 여부'"
    )
    val deleted: Boolean = false,
    @Column(
        name = "options",
        nullable = false,
        columnDefinition = "JSON not null comment '선택지 옵션'"
    )
    @Convert(converter = ListToJsonConverter::class)
    val options: List<String>,
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "form_id",
        columnDefinition = "char(26) not null comment '설문 ID'",
        referencedColumnName = "id",
        nullable = false,
        foreignKey = ForeignKey(name = "fk_question_template_form", value = ConstraintMode.NO_CONSTRAINT)
    )
    val formEntity: FormEntity,
) : Persistable<String> {

    init {
        validateRequiredFields()
    }

    private fun validateRequiredFields() {
        QuestionTemplateValidator.validateVersion(version)
        QuestionTemplateValidator.validateDisplayOrder(displayOrder)
    }

    fun toDomain(): Question {
        return Question(
            id = id,
            version = version,
            required = required,
            displayOrder = displayOrder,
            type = InputType.valueOrThrows(type),
            title = title,
            description = description,
            deleted = deleted,
            options = options.toList(),
            formId = formEntity.id
        )
    }

    override fun getId(): String {
        return id
    }

    override fun isNew(): Boolean {
        return id.isBlank()
    }

    @PrePersist
    fun prePersist() {
        if (id.isBlank()) {
            id = IdGenerator.next()
        }
    }

    companion object {
        fun from(question: Question, formEntity: FormEntity): QuestionEntity {
            return QuestionEntity(
                id = question.id,
                version = question.version,
                required = question.required,
                displayOrder = question.displayOrder,
                type = question.type.name,
                title = question.title,
                description = question.description,
                deleted = question.deleted,
                options = question.options.toList(),
                formEntity = formEntity
            )
        }
    }
}
