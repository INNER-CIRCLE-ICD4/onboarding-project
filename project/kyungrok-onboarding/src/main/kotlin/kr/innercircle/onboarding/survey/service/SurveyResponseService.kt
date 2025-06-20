package kr.innercircle.onboarding.survey.service

import kr.innercircle.onboarding.survey.domain.Survey
import kr.innercircle.onboarding.survey.domain.SurveyResponse
import kr.innercircle.onboarding.survey.dto.request.CreateSurveyResponseRequest
import kr.innercircle.onboarding.survey.repository.SurveyResponseRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * packageName : kr.innercircle.onboarding.survey.service
 * fileName    : SurveyResponseService
 * author      : ckr
 * date        : 25. 6. 21.
 * description :
 */

@Service
class SurveyResponseService(
    private val surveyResponseRepository: SurveyResponseRepository,
    private val surveyResponseAnswerService: SurveyResponseAnswerService
) {

    @Transactional
    fun createSurveyResponses(
        survey: Survey,
        createSurveyResponseRequest: CreateSurveyResponseRequest
    ): SurveyResponse {
        val surveyResponse = surveyResponseRepository.save(
            SurveyResponse.from(survey, createSurveyResponseRequest)
        )

        surveyResponseAnswerService.createSurveyResponseAnswers(surveyResponse, createSurveyResponseRequest.answers)
        return surveyResponse
    }
}