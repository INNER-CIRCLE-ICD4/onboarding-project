package fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa.entity

import fc.innercircle.sanghyukonboarding.common.domain.model.IdGenerator
import fc.innercircle.sanghyukonboarding.form.domain.model.QuestionSnapshot
import fc.innercircle.sanghyukonboarding.form.domain.model.SelectableOption
import fc.innercircle.sanghyukonboarding.form.domain.model.vo.InputType
import fc.innercircle.sanghyukonboarding.form.domain.validator.QuestionSnapshotValidator
import jakarta.persistence.Column
import jakarta.persistence.ConstraintMode
import jakarta.persistence.Entity
import jakarta.persistence.EnumType.STRING
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.PrePersist
import jakarta.persistence.Table
import org.springframework.data.domain.Persistable

@Table(name = "question_snapshot",
    indexes = [],
    uniqueConstraints = [])
@Entity(name = "question_snapshot")
open class QuestionSnapshotEntity(
    @Id
    @Column(
        name = "id",
        nullable = false,
        updatable = false,
        unique = true,
        columnDefinition = "char(26) not null comment 'ID'"
    )
    private var id: String = "",
    @Column(nullable = false, length = 500, columnDefinition = "varchar(500) comment '질문 제목'")
    val title: String,
    @Column(
        nullable = false,
        length = 1000,
        columnDefinition = "varchar(1000) comment '질문 설명'"
    )
    val description: String = "",
    @Enumerated(value = STRING)
    @Column(nullable = false, columnDefinition = "varchar(20) comment '질문 입력 타입'")
    val type: InputType,
    @Column(nullable = false, columnDefinition = "int not null comment '질문 버전'")
    val version: Long,
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "question_template_id",
        referencedColumnName = "id",
        nullable = false,
        foreignKey = ForeignKey(
            name = "fk_question_snapshot_question_template_id",
            value = ConstraintMode.NO_CONSTRAINT
        )
    )
    val questionTemplateEntity: QuestionTemplateEntity,
): Persistable<String> {

    init {
        validateRequiredFields()
    }

    private fun validateRequiredFields() {
        QuestionSnapshotValidator.validateTitle(title)
        QuestionSnapshotValidator.validateDescription(description)
        QuestionSnapshotValidator.validateVersion(version)
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

    fun toDomain(selectableOptions: List<SelectableOption>): QuestionSnapshot {
        return QuestionSnapshot(
            id = id,
            title = title,
            description = description,
            type = type,
            version = version,
            selectableOptions = selectableOptions.filter { it.questionSnapshotId == id },
            questionTemplateId = questionTemplateEntity.id,
        )
    }

    companion object {
        fun from(snapshot: QuestionSnapshot, questionTemplateEntity: QuestionTemplateEntity): QuestionSnapshotEntity {
            return QuestionSnapshotEntity(
                id = snapshot.id,
                title = snapshot.title,
                description = snapshot.description,
                type = snapshot.type,
                version = snapshot.version,
                questionTemplateEntity = questionTemplateEntity,
            )
        }
    }
}
