package fc.innercircle.sanghyukonboarding.survey.domain.model

import fc.innercircle.sanghyukonboarding.common.domain.model.BaseEntity
import fc.innercircle.sanghyukonboarding.survey.domain.validator.ItemOptionsValidator
import jakarta.persistence.Column
import jakarta.persistence.ConstraintMode
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
open class ItemOptions(
    optionText: String,
    displayOrder: Int = 0,
    surveyItem: SurveyItem,
    createdBy: String = "system",
) : BaseEntity(createdBy) {

    @Column(nullable = false, length = 50, columnDefinition = "varchar(50) not null comment '설문 선택 옵션'")
    var optionText: String = optionText
        protected set

    @Column(nullable = false, columnDefinition = "int default 0 not null comment '항목 순서'")
    var displayOrder: Int = displayOrder
        protected set

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "survey_item_id",
        nullable = false,
        foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT),
        columnDefinition = "bigint not null comment '설문 항목 ID'"
    )
    var surveyItem: SurveyItem = surveyItem
        protected set

    init {
        validateRequiredFields()
    }

    private fun validateRequiredFields() {
        ItemOptionsValidator.validateOptionText(optionText)
        ItemOptionsValidator.validateDisplayOrder(displayOrder)
    }
}
