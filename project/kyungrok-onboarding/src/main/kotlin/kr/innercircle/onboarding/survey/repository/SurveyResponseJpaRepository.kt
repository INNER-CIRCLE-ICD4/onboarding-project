package kr.innercircle.onboarding.survey.repository

import kr.innercircle.onboarding.survey.domain.Survey
import kr.innercircle.onboarding.survey.domain.SurveyResponse
import org.springframework.data.jpa.repository.JpaRepository

/**
 * packageName : kr.innercircle.onboarding.survey.repository
 * fileName    : SurveyResponseJpaRepository
 * author      : ckr
 * date        : 25. 6. 21.
 * description :
 */
interface SurveyResponseJpaRepository: JpaRepository<SurveyResponse, Long> {
    fun findAllBySurvey(survey: Survey): List<SurveyResponse>
}