package kr.innercircle.onboarding.survey.repository

import kr.innercircle.onboarding.survey.domain.SurveyResponse
import kr.innercircle.onboarding.survey.domain.SurveyResponseAnswer

/**
 * packageName : kr.innercircle.onboarding.survey.repository
 * fileName    : SurveyResponseAnswerRepository
 * author      : ckr
 * date        : 25. 6. 21.
 * description :
 */
interface SurveyResponseAnswerRepository {
    fun saveAll(surveyResponseAnswers: List<SurveyResponseAnswer>): List<SurveyResponseAnswer>
    fun findAllBySurveyResponse(surveyResponse: SurveyResponse): List<SurveyResponseAnswer>
}