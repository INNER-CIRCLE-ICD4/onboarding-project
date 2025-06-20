package kr.innercircle.onboarding.survey.repository

import kr.innercircle.onboarding.survey.domain.Survey

/**
 * packageName : kr.innercircle.onboarding.survey.repository
 * fileName    : SurveyRepository
 * author      : ckr
 * date        : 25. 6. 16.
 * description :
 */
interface SurveyRepository {
    fun save(survey: Survey): Survey
    fun findById(id: Long): Survey?
    fun findAll(): List<Survey>
}