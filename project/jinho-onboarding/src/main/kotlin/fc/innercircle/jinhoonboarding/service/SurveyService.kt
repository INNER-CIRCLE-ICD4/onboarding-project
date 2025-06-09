package fc.innercircle.jinhoonboarding.service

import fc.innercircle.jinhoonboarding.repository.SurveyRepository
import org.springframework.stereotype.Service

@Service
class SurveyService(
    private val surveyRepository: SurveyRepository
) {

}