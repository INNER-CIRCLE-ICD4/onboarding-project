package kr.innercircle.onboarding.survey.domain

import jakarta.persistence.*
import kr.innercircle.onboarding.survey.dto.request.CreateSurveyItemOptionRequest
import kr.innercircle.onboarding.survey.exception.InvalidSurveyItemTypeException

/**
 * packageName : kr.innercircle.onboarding.survey.domain
 * fileName    : SurveyItemOption
 * author      : ckr
 * date        : 25. 6. 12.
 * description :
 */

@Table(name = "survey_item_option")
@Entity
class SurveyItemOption (
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_item_id", nullable = false)
    var surveyItem: SurveyItem,

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, length = 200)
    var option: String,

    @Column(nullable = false)
    var orderNumber: Int
): BaseTimeEntity() {
    companion object {
        fun of(
            surveyItem: SurveyItem,
            createSurveyItemOptionRequest: CreateSurveyItemOptionRequest,
            orderNumber: Int
        ): SurveyItemOption {
            if(surveyItem.inputType != SurveyItemInputType.SINGLE_CHOICE && surveyItem.inputType != SurveyItemInputType.MULTIPLE_CHOICE) {
                throw InvalidSurveyItemTypeException()
            }

            return SurveyItemOption(
                surveyItem = surveyItem,
                option = createSurveyItemOptionRequest.option,
                orderNumber = orderNumber
            )
        }
    }
}