package fc.innercircle.sanghyukonboarding.surveyreply.model

import fc.innercircle.sanghyukonboarding.common.domain.model.BaseEntity
import fc.innercircle.sanghyukonboarding.surveyreply.model.vo.SelectedItemOptions
import fc.innercircle.sanghyukonboarding.surveyreply.validator.SurveyItemAnswerValidator
import jakarta.persistence.Column
import jakarta.persistence.ConstraintMode
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
open class SurveyItemAnswer(
    surveyReply: SurveyReply,
    surveyItemId: Long,
    question: String,
    description: String,
    required: Boolean = false,
    surveyInputType: String,
    answer: String = "",
    selectedItemOptions: List<String> = emptyList(),
    createdBy: String,
) : BaseEntity(createdBy) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long = 0L

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "survey_reply_id",
        nullable = false,
        foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT),
        columnDefinition = "bigint not null comment '설문 응답 ID'"
    )
    var surveyReply: SurveyReply = surveyReply
        protected set

    @Column(name = "survey_item_id", nullable = false, columnDefinition = "bigint not null comment '설문 항목 ID'")
    var surveyItemId: Long = surveyItemId
        protected set

    @Column(nullable = false, length = 500, columnDefinition = "varchar(500) comment '설문 항목 제목'")
    var question: String = question
        protected set

    @Column(nullable = false, length = 1000, columnDefinition = "varchar(1000) comment '설문 항목 설명'")
    var description: String = description
        protected set

    @Column(nullable = false, columnDefinition = "tinyint(1) not null comment '설문 항목 입력 필수 여부'")
    var required: Boolean = required
        protected set

    @Column(nullable = false, columnDefinition = "varchar(20) comment '설문 항목 입력 타입'")
    var type: String = surveyInputType
        protected set

    @Column(nullable = false, columnDefinition = "TEXT comment '설문 항목 응답 내용(단답형, 장문형)'")
    var answer: String = answer
        protected set

    @Embedded
    var selectedItemOptions: SelectedItemOptions = SelectedItemOptions(selectedItemOptions)
        protected set

    init {
        validateRequiredFields()
    }

    private fun validateRequiredFields() {
        SurveyItemAnswerValidator.validateQuestion(question)
        SurveyItemAnswerValidator.validateDescription(description)
        SurveyItemAnswerValidator.validateSurveyInputType(type)
        SurveyItemAnswerValidator.validateAnswer(answer)
    }
}
