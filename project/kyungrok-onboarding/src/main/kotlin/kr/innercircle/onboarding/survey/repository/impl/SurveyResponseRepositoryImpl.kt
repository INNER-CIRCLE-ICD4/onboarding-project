package kr.innercircle.onboarding.survey.repository.impl

import kr.innercircle.onboarding.survey.domain.SurveyResponse
import kr.innercircle.onboarding.survey.repository.SurveyResponseJpaRepository
import kr.innercircle.onboarding.survey.repository.SurveyResponseRepository
import org.springframework.stereotype.Repository

/**
 * packageName : kr.innercircle.onboarding.survey.repository.impl
 * fileName    : SurveyResponseRepositoryImpl
 * author      : ckr
 * date        : 25. 6. 21.
 * description :
 */

@Repository
class SurveyResponseRepositoryImpl(
    private val surveyResponseJpaRepository: SurveyResponseJpaRepository
): SurveyResponseRepository {
    override fun save(surveyResponse: SurveyResponse): SurveyResponse = surveyResponseJpaRepository.save(surveyResponse)
}