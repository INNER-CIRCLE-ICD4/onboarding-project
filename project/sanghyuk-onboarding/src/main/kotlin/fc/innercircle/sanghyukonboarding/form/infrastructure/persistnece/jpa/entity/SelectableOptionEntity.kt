package fc.innercircle.sanghyukonboarding.form.infrastructure.persistnece.jpa.entity

import fc.innercircle.sanghyukonboarding.common.domain.model.IdGenerator
import fc.innercircle.sanghyukonboarding.form.domain.model.SelectableOption
import fc.innercircle.sanghyukonboarding.form.domain.validator.SelectableOptionsValidator
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

@Table(
    name = "selectable_option",
    indexes = [],
    uniqueConstraints = []
)
@Entity(name = "selectable_option")
open class SelectableOptionEntity(
    @Id
    @Column(
        name = "id",
        nullable = false,
        updatable = false,
        unique = true,
        columnDefinition = "char(26) comment 'ID'"
    )
    private var id: String? = null,
    @Column(name = "text", nullable = false, length = 50, columnDefinition = "varchar(50) not null comment '질문 선택 옵션'")
    val text: String,
    @Column(nullable = false, columnDefinition = "int default 0 not null comment '항목 순서'")
    val displayOrder: Int,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "question_snapshot_id",
        referencedColumnName = "id",
        nullable = false,
        updatable = false,
        foreignKey = ForeignKey(
            value = ConstraintMode.NO_CONSTRAINT
        )
    )
    val questionSnapshotEntity: QuestionSnapshotEntity,
): Persistable<String> {

    init {
        validateRequiredFields()
    }

    private fun validateRequiredFields() {
        SelectableOptionsValidator.validateText(text)
        SelectableOptionsValidator.validateDisplayOrder(displayOrder)
    }

    fun toDomain(): SelectableOption {
        return SelectableOption(
            id = id!!,
            text = text,
            displayOrder = displayOrder,
            questionSnapshotId = questionSnapshotEntity.id,
        )
    }

    override fun getId(): String {
        return id ?: throw IllegalStateException("cannot get ID before persist")
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
        fun from(
            selectableOption: SelectableOption,
            questionSnapshotEntity: QuestionSnapshotEntity,
        ): SelectableOptionEntity {
            return SelectableOptionEntity(
                text = selectableOption.text,
                displayOrder = selectableOption.displayOrder,
                questionSnapshotEntity = questionSnapshotEntity,
            )
        }
    }
}
