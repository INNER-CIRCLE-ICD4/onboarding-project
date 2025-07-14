package kr.innercircle.onboarding.survey.dto.response

import kr.innercircle.onboarding.survey.domain.SurveyResponseAnswer
import java.time.LocalDateTime

/**
 * packageName : kr.innercircle.onboarding.survey.dto.response
 * fileName    : GetSurveyItemsAnswerResponse
 * author      : ckr
 * date        : 25. 6. 21.
 * description :
 */
data class GetSurveyItemsAnswerResponse(
    val surveyItemId: Long,
    val respondent: String,
    val answer: String?,
    val answeredAt: LocalDateTime
) {
    constructor(surveyResponseAnswer: SurveyResponseAnswer) : this(
        surveyItemId = surveyResponseAnswer.surveyItem.id!!,
        respondent = surveyResponseAnswer.surveyResponse.respondent,
        answer = surveyResponseAnswer.answer,
        answeredAt = surveyResponseAnswer.createdAt
    )
}
