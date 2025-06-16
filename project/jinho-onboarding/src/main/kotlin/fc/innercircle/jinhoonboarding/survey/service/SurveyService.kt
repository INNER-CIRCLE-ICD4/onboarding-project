package fc.innercircle.jinhoonboarding.survey.service

import fc.innercircle.jinhoonboarding.survey.dto.request.SurveyRequest
import fc.innercircle.jinhoonboarding.survey.dto.response.SurveyResponse
import fc.innercircle.jinhoonboarding.survey.repository.SurveyRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class SurveyService(
    private val surveyRepository: SurveyRepository
) {

    fun getSurvey(surveyId: Long): SurveyResponse {
        return SurveyResponse.fromDomain(surveyRepository.findById(surveyId).orElseThrow{ RuntimeException("설문 ID가  $surveyId 인 설문은 존재하지 않습니다.") })
    }

    fun createSurvey(request: SurveyRequest): SurveyResponse {
        val surveyToCreate = request.toDomain()
        return SurveyResponse.fromDomain(surveyRepository.save(surveyToCreate))
    }
    @Transactional
    fun updateSurvey(request: SurveyRequest, surveyId: Long): SurveyResponse {
        // 수정 할 대상 설문을 가져온다
        val surveyToUpdate = surveyRepository.findById(surveyId).orElseThrow{ RuntimeException("수정할 설문이 없습니다: $surveyId") }

        val tobeSurvey = request.toDomain()
        val questions = tobeSurvey.questions

        surveyToUpdate.updateTitle(tobeSurvey.title)
        surveyToUpdate.updateDescription(tobeSurvey.description)
        surveyToUpdate.updateQuestions(questions)

        return SurveyResponse.fromDomain(surveyRepository.save(surveyToUpdate))
    }

    fun deleteById(surveyId: Long) {
        surveyRepository.findById(surveyId).orElseThrow{ RuntimeException("삭제할 설문이 없습니다: $surveyId") }
        surveyRepository.deleteById(surveyId)
    }

}