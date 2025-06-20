package kr.innercircle.onboarding.survey.domain

import jakarta.persistence.*
import kr.innercircle.onboarding.survey.dto.request.CreateSurveyItemRequest
import kr.innercircle.onboarding.survey.dto.request.UpdateSurveyItemRequest

/**
 * packageName : kr.innercircle.onboarding.survey.domain
 * fileName    : SurveyItem
 * author      : ckr
 * date        : 25. 6. 12.
 * description :
 */

@Table(name = "survey_item")
@Entity
class SurveyItem (
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    var survey: Survey,

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, length = 200)
    var name: String,

    @Column(columnDefinition = "TEXT")
    var description: String? = null,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var inputType: SurveyItemInputType,

    @Column(nullable = false)
    var isRequired: Boolean,

    @Column(nullable = false)
    var orderNumber: Int,

    @Column(nullable = false)
    var isDeleted: Boolean = false
): BaseTimeEntity() {
    fun hasDeleted() {
        this.isDeleted = true
    }

    companion object {
        fun from(
            survey: Survey,
            createSurveyItemRequest: CreateSurveyItemRequest,
            orderNumber: Int
        ): SurveyItem {
            return SurveyItem(
                survey = survey,
                name = createSurveyItemRequest.name,
                description = createSurveyItemRequest.description,
                inputType = createSurveyItemRequest.inputType,
                isRequired = createSurveyItemRequest.isRequired,
                orderNumber = orderNumber
            )
        }

        fun from(
            survey: Survey,
            updateSurveyItemRequest: UpdateSurveyItemRequest,
            orderNumber: Int
        ): SurveyItem {
            return SurveyItem(
                survey = survey,
                name = updateSurveyItemRequest.name,
                description = updateSurveyItemRequest.description,
                inputType = updateSurveyItemRequest.inputType,
                isRequired = updateSurveyItemRequest.isRequired,
                orderNumber = orderNumber
            )
        }
    }
}