package kr.innercircle.onboarding.survey.repository.impl

import kr.innercircle.onboarding.survey.domain.SurveyResponseAnswer
import kr.innercircle.onboarding.survey.repository.SurveyResponseAnswerJpaRepository
import kr.innercircle.onboarding.survey.repository.SurveyResponseAnswerRepository
import org.springframework.stereotype.Repository

/**
 * packageName : kr.innercircle.onboarding.survey.repository.impl
 * fileName    : SurveyResponseAnswerRepositoryImpl
 * author      : ckr
 * date        : 25. 6. 21.
 * description :
 */

@Repository
class SurveyResponseAnswerRepositoryImpl(
    private val surveyResponseAnswerJpaRepository: SurveyResponseAnswerJpaRepository
): SurveyResponseAnswerRepository {
    override fun saveAll(surveyResponseAnswers: List<SurveyResponseAnswer>): List<SurveyResponseAnswer> = surveyResponseAnswerJpaRepository.saveAll(surveyResponseAnswers)
}