package fc.innercircle.jinhoonboarding.survey.service

import fc.innercircle.jinhoonboarding.survey.dto.request.CreateSurveyRequest
import fc.innercircle.jinhoonboarding.survey.domain.Survey
import fc.innercircle.jinhoonboarding.survey.dto.response.SurveyResponse
import fc.innercircle.jinhoonboarding.survey.repository.SurveyRepository
import org.springframework.stereotype.Service

@Service
class SurveyService(
    private val surveyRepository: SurveyRepository
) {

    // 설문조사 조회
    fun getSurvey(surveyId: Long): SurveyResponse {
        return SurveyResponse.fromDomain(surveyRepository.findById(surveyId).orElseThrow{ RuntimeException("설문 ID가  $surveyId 인 설문은 존재하지 않습니다.") })
    }

    // 설문조사 생성
    fun createSurvey(request: CreateSurveyRequest): SurveyResponse {
        val surveyToCreate = request.toDomain()
        return SurveyResponse.fromDomain(surveyRepository.save(surveyToCreate))
    }

}