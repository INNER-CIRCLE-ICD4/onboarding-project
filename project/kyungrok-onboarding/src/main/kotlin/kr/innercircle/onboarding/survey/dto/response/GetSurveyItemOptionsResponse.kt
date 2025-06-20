package kr.innercircle.onboarding.survey.dto.response

import kr.innercircle.onboarding.survey.domain.SurveyItemOption

/**
 * packageName : kr.innercircle.onboarding.survey.dto.response
 * fileName    : GetSurveyItemOptionsResponse
 * author      : ckr
 * date        : 25. 6. 21.
 * description :
 */

data class GetSurveyItemOptionsResponse(
    val surveyItemOptionId: Long,
    val option: String,
    val orderNumber: Int,
) {
    constructor(surveyItemOption: SurveyItemOption) : this(
        surveyItemOptionId = surveyItemOption.id!!,
        option = surveyItemOption.option,
        orderNumber = surveyItemOption.orderNumber
    )
}
