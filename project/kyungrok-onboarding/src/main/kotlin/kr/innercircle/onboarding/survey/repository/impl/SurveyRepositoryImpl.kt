package kr.innercircle.onboarding.survey.repository.impl

import kr.innercircle.onboarding.survey.domain.Survey
import kr.innercircle.onboarding.survey.repository.SurveyJpaRepository
import kr.innercircle.onboarding.survey.repository.SurveyRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

/**
 * packageName : kr.innercircle.onboarding.survey.repository.impl
 * fileName    : SurveyRepositoryImpl
 * author      : ckr
 * date        : 25. 6. 16.
 * description :
 */

@Repository
class SurveyRepositoryImpl(
    private val surveyJpaRepository: SurveyJpaRepository
): SurveyRepository {
    override fun save(survey: Survey): Survey = surveyJpaRepository.save(survey)
    override fun findById(id: Long): Survey? = surveyJpaRepository.findByIdOrNull(id)
    override fun findAll(): List<Survey> = surveyJpaRepository.findAll()
}