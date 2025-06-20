package kr.innercircle.onboarding.survey.dto.response

import kr.innercircle.onboarding.survey.domain.Survey

/**
 * packageName : kr.innercircle.onboarding.survey.dto.response
 * fileName    : GetSurveysAnswerResponse
 * author      : ckr
 * date        : 25. 6. 21.
 * description :
 */
data class GetSurveysAnswerResponse(
    val surveyId: Long,
    val name: String,
    val description: String?,
    val items: List<GetSurveyItemsResponse>,
    val answers: List<GetSurveyItemsAnswerResponse>
) {
    constructor(
        survey: Survey,
        surveyItemsResponses: List<GetSurveyItemsResponse>,
        surveyItemsAnswerResponses: List<GetSurveyItemsAnswerResponse>
    ) : this(
        surveyId = survey.id!!,
        name = survey.name,
        description = survey.description,
        items = surveyItemsResponses,
        answers = surveyItemsAnswerResponses
    )
}
