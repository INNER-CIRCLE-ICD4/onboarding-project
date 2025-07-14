package kr.innercircle.onboarding.survey.repository

import kr.innercircle.onboarding.survey.domain.SurveyResponse
import kr.innercircle.onboarding.survey.domain.SurveyResponseAnswer
import org.springframework.data.jpa.repository.JpaRepository

/**
 * packageName : kr.innercircle.onboarding.survey.repository
 * fileName    : SurveyResponseAnswerJpaRepository
 * author      : ckr
 * date        : 25. 6. 21.
 * description :
 */
interface SurveyResponseAnswerJpaRepository: JpaRepository<SurveyResponseAnswer, Long> {
    fun findAllBySurveyResponse(surveyResponse: SurveyResponse): List<SurveyResponseAnswer>
}