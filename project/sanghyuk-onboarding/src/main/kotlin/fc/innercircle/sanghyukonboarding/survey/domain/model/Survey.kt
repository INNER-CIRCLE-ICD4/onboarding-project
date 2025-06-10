package fc.innercircle.sanghyukonboarding.survey.domain.model

import fc.innercircle.sanghyukonboarding.common.domain.model.BaseEntity
import fc.innercircle.sanghyukonboarding.survey.domain.validator.SurveyValidator
import jakarta.persistence.Column
import jakarta.persistence.Entity

@Entity
open class Survey(
    title: String,
    description: String = "",
    createdBy: String,
) : BaseEntity(createdBy) {

    @Column(nullable = false, length = 255, columnDefinition = "varchar(255) comment '설문 제목'")
    var title: String = title
        protected set

    @Column(nullable = false, length = 1000, columnDefinition = "varchar(1000) comment '설문 설명'")
    var description: String = description
        protected set

    init {
        validateRequiredFields()
    }

    private fun validateRequiredFields() {
        SurveyValidator.validateTitle(title)
        SurveyValidator.validateDescription(description)
    }
}
