package fc.innercircle.sanghyukonboarding.survey.domain.model

import fc.innercircle.sanghyukonboarding.common.domain.model.BaseEntity
import fc.innercircle.sanghyukonboarding.survey.domain.validator.SurveyItemValidator
import jakarta.persistence.Column
import jakarta.persistence.ConstraintMode
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
open class SurveyItem(
    question: String,
    description: String = "",
    type: ItemType,
    required: Boolean = false,
    displayOrder: Int = 0,
    survey: Survey,
    createdBy: String,
) : BaseEntity(createdBy) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long = 0L
        protected set

    @Column(nullable = false, length = 500, columnDefinition = "varchar(500) comment '설문 항목 제목'")
    var question: String = question
        protected set

    @Column(nullable = false, length = 1000, columnDefinition = "varchar(1000) comment '설문 항목 설명'")
    var description: String = description
        protected set

    @Column(nullable = false, columnDefinition = "varchar(20) comment '설문 항목 타입'")
    var type: String = type.name
        protected set

    @Column(nullable = false, columnDefinition = "boolean default false comment '필수 여부'")
    var required: Boolean = required
        protected set

    @Column(nullable = false, columnDefinition = "int default 0 comment '항목 순서'")
    var displayOrder: Int = displayOrder
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "survey_id",
        nullable = false,
        columnDefinition = "bigint",
        foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT),
    )
    var survey: Survey = survey
        protected set

    init {
        validateRequiredFields()
    }

    private fun validateRequiredFields() {
        SurveyItemValidator.validateQuestion(question)
        SurveyItemValidator.validateDescription(description)
        SurveyItemValidator.validateRequired(required)
        SurveyItemValidator.validateDisplayOrder(displayOrder)
    }

    enum class ItemType {
        TEXT, // 단답형
        LONG_TEXT, // 장문형
        RADIO, // 라디오 버튼
        CHECKBOX, // 체크박스
        SELECT, // 드롭다운,

        ;

        companion object {
            fun fromString(type: String): ItemType = valueOf(type.uppercase())
        }
    }
}
