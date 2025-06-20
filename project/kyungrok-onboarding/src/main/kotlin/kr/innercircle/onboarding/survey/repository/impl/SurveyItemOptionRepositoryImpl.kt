package kr.innercircle.onboarding.survey.repository.impl

import kr.innercircle.onboarding.survey.domain.SurveyItem
import kr.innercircle.onboarding.survey.domain.SurveyItemOption
import kr.innercircle.onboarding.survey.repository.SurveyItemOptionJpaRepository
import kr.innercircle.onboarding.survey.repository.SurveyItemOptionRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

/**
 * packageName : kr.innercircle.onboarding.survey.repository.impl
 * fileName    : SurveyItemOptionRepositoryImpl
 * author      : ckr
 * date        : 25. 6. 17.
 * description :
 */

@Repository
class SurveyItemOptionRepositoryImpl(
    private val surveyItemOptionJpaRepository: SurveyItemOptionJpaRepository
): SurveyItemOptionRepository {
    override fun findById(id: Long): SurveyItemOption? = surveyItemOptionJpaRepository.findByIdOrNull(id)
    override fun findAll(): List<SurveyItemOption> = surveyItemOptionJpaRepository.findAll()
    override fun saveAll(surveyItemOptions: List<SurveyItemOption>): List<SurveyItemOption> = surveyItemOptionJpaRepository.saveAll(surveyItemOptions)
    override fun findAllBySurveyItemOrderByOrderNumber(surveyItem: SurveyItem): List<SurveyItemOption> = surveyItemOptionJpaRepository.findAllBySurveyItemOrderByOrderNumber(surveyItem)
}