package kr.innercircle.onboarding.survey.repository.impl

import kr.innercircle.onboarding.survey.domain.Survey
import kr.innercircle.onboarding.survey.domain.SurveyItem
import kr.innercircle.onboarding.survey.repository.SurveyItemJpaRepository
import kr.innercircle.onboarding.survey.repository.SurveyItemRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

/**
 * packageName : kr.innercircle.onboarding.survey.repository.impl
 * fileName    : SurveyItemRepositoryImpl
 * author      : ckr
 * date        : 25. 6. 16.
 * description :
 */

@Repository
class SurveyItemRepositoryImpl(
    private val surveyItemJpaRepository: SurveyItemJpaRepository
): SurveyItemRepository {
    override fun save(surveyItem: SurveyItem): SurveyItem = surveyItemJpaRepository.save(surveyItem)
    override fun findById(id: Long): SurveyItem? = surveyItemJpaRepository.findByIdOrNull(id)
    override fun findAll(): List<SurveyItem> = surveyItemJpaRepository.findAll()
    override fun findAllBySurveyAndIsDeleted(survey: Survey, isDeleted: Boolean): List<SurveyItem> = surveyItemJpaRepository.findAllBySurveyAndIsDeleted(survey, isDeleted)
}