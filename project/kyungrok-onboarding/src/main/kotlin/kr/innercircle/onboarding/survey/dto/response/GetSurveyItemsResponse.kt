package kr.innercircle.onboarding.survey.dto.response

import kr.innercircle.onboarding.survey.domain.SurveyItem
import kr.innercircle.onboarding.survey.domain.SurveyItemInputType

/**
 * packageName : kr.innercircle.onboarding.survey.dto.response
 * fileName    : GetSurveyItemsResponse
 * author      : ckr
 * date        : 25. 6. 21.
 * description :
 */
data class GetSurveyItemsResponse(
    val surveyItemId: Long,
    val name: String,
    val description: String?,
    val inputType: SurveyItemInputType,
    val isRequired: Boolean,
    val orderNumber: Int,
    val options: List<GetSurveyItemOptionsResponse>
) {
    constructor(surveyItem: SurveyItem, surveyItemOptionsResponses: List<GetSurveyItemOptionsResponse>) : this(
        surveyItemId = surveyItem.id!!,
        name = surveyItem.name,
        description = surveyItem.description,
        inputType = surveyItem.inputType,
        isRequired = surveyItem.isRequired,
        orderNumber = surveyItem.orderNumber,
        options = surveyItemOptionsResponses
    )
}
