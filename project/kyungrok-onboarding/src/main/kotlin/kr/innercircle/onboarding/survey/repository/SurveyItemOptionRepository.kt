package kr.innercircle.onboarding.survey.repository

import kr.innercircle.onboarding.survey.domain.SurveyItemOption

/**
 * packageName : kr.innercircle.onboarding.survey.repository
 * fileName    : SurveyItemOptionRepository
 * author      : ckr
 * date        : 25. 6. 17.
 * description :
 */
interface SurveyItemOptionRepository {
    fun findById(id: Long): SurveyItemOption?
    fun findAll(): List<SurveyItemOption>
    fun saveAll(surveyItemOptions: List<SurveyItemOption>): List<SurveyItemOption>
}