package kr.innercircle.onboarding.survey.repository

import kr.innercircle.onboarding.survey.domain.SurveyItem

/**
 * packageName : kr.innercircle.onboarding.survey.repository
 * fileName    : SurveyItemRepository
 * author      : ckr
 * date        : 25. 6. 16.
 * description :
 */
interface SurveyItemRepository {
    fun save(surveyItem: SurveyItem): SurveyItem
    fun findById(id: Long): SurveyItem?
    fun findAll(): List<SurveyItem>
}