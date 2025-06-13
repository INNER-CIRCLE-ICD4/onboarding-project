package fc.innercircle.sanghyukonboarding.form.domain.model

import fc.innercircle.sanghyukonboarding.common.domain.model.BaseEntity
import fc.innercircle.sanghyukonboarding.form.domain.validator.SelectableOptionsValidator
import jakarta.persistence.Column
import jakarta.persistence.ConstraintMode
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
open class SelectableOption(
    value: String,
    displayOrder: Int = 0,
    questionSnapshot: QuestionSnapshot,
    createdBy: String = "system",
) : BaseEntity(createdBy) {

    @Column(nullable = false, length = 50, columnDefinition = "varchar(50) not null comment '질문 선택 옵션'")
    var value: String = value
        protected set

    @Column(nullable = false, columnDefinition = "int default 0 not null comment '항목 순서'")
    var displayOrder: Int = displayOrder
        protected set

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "question_snapshot_id",
        nullable = false,
        foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT),
        columnDefinition = "bigint not null comment '질문 스냅샷 ID'"
    )
    var questionSnapshot: QuestionSnapshot = questionSnapshot
        protected set

    init {
        validateRequiredFields()
    }

    private fun validateRequiredFields() {
        SelectableOptionsValidator.validateValue(value)
        SelectableOptionsValidator.validateDisplayOrder(displayOrder)
    }
}
