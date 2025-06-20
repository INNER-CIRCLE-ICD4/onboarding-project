package kr.innercircle.onboarding.survey.dto.response

import kr.innercircle.onboarding.survey.domain.Survey

/**
 * packageName : kr.innercircle.onboarding.survey.dto.response
 * fileName    : GetSurveysResponse
 * author      : ckr
 * date        : 25. 6. 21.
 * description :
 */
data class GetSurveysResponse(
    val surveyId: Long,
    val name: String,
    val description: String?,
    val items: List<GetSurveyItemsResponse>
) {
    constructor(survey: Survey, surveyItemsResponses: List<GetSurveyItemsResponse>) : this(
        surveyId = survey.id!!,
        name = survey.name,
        description = survey.description,
        items = surveyItemsResponses
    )
}
