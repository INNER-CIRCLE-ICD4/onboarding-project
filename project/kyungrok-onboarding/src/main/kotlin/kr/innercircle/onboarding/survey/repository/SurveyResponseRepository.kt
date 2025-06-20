package kr.innercircle.onboarding.survey.repository

import kr.innercircle.onboarding.survey.domain.SurveyResponse

/**
 * packageName : kr.innercircle.onboarding.survey.repository
 * fileName    : SurveyResponseRepository
 * author      : ckr
 * date        : 25. 6. 21.
 * description :
 */
interface SurveyResponseRepository {
    fun save(surveyResponse: SurveyResponse): SurveyResponse
}