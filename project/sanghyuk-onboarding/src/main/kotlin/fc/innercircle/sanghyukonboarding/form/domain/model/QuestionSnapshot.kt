package fc.innercircle.sanghyukonboarding.form.domain.model

import fc.innercircle.sanghyukonboarding.common.domain.model.BaseEntity
import fc.innercircle.sanghyukonboarding.form.domain.model.vo.InputType
import fc.innercircle.sanghyukonboarding.form.domain.validator.QuestionSnapshotValidator
import jakarta.persistence.Column
import jakarta.persistence.ConstraintMode
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
open class QuestionSnapshot(
    title: String,
    description: String = "",
    type: InputType,
    required: Boolean = false,
    displayOrder: Int = 0,
    version: Long,
    createdBy: String,
    questionTemplate: QuestionTemplate,
) : BaseEntity(createdBy) {

    @Column(nullable = false, length = 500, columnDefinition = "varchar(500) comment '질문 제목'")
    var title: String = title
        protected set

    @Column(nullable = false, length = 1000, columnDefinition = "varchar(1000) comment '질문 설명'")
    var description: String = description
        protected set

    @Column(nullable = false, columnDefinition = "varchar(20) comment '질문 입력 타입'")
    var type: String = type.name
        protected set

    @Column(nullable = false, columnDefinition = "boolean default false comment '필수 여부'")
    var required: Boolean = required
        protected set

    @Column(nullable = false, columnDefinition = "int default 0 comment '항목 순서'")
    var displayOrder: Int = displayOrder
        protected set

    @Column(nullable = false, columnDefinition = "bigint default 0 not null comment '버전'")
    var version: Long = version
        protected set

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(
        name = "question_template_id",
        nullable = false,
        columnDefinition = "char(26) not null comment '질문 ID'",
        foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT)
    )
    var questionTemplate: QuestionTemplate = questionTemplate
        protected set

    init {
        validateRequiredFields()
    }

    private fun validateRequiredFields() {
        QuestionSnapshotValidator.validateTitle(title)
        QuestionSnapshotValidator.validateDescription(description)
        QuestionSnapshotValidator.validateDisplayOrder(displayOrder)
        QuestionSnapshotValidator.validateVersion(version)
    }
}
