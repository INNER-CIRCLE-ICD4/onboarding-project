package fc.innercircle.sanghyukonboarding.form.domain.model

import fc.innercircle.sanghyukonboarding.common.domain.model.BaseEntity
import fc.innercircle.sanghyukonboarding.form.domain.validator.QuestionTemplateValidator
import jakarta.persistence.Column
import jakarta.persistence.ConstraintMode
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
open class QuestionTemplate(
    form: Form,
    version: Long = 0L,
    createdBy: String,
) : BaseEntity(createdBy) {

    @Column(nullable = false, columnDefinition = "bigint default 0 not null comment '버전'")
    var version: Long = version
        protected set

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(
        name = "form_id",
        nullable = false,
        columnDefinition = "char(26) not null comment '설문 ID'",
        foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT)
    )
    var form: Form = form
        protected set

    init {
        validateRequiredFields()
    }

    private fun validateRequiredFields() {
        QuestionTemplateValidator.validateVersion(version)
    }
}
