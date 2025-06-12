package fc.innercircle.jinhoonboarding.survey.service

import fc.innercircle.jinhoonboarding.common.util.toEnumOrNull
import fc.innercircle.jinhoonboarding.survey.dto.CreateSurveyRequest
import fc.innercircle.jinhoonboarding.survey.domain.Question
import fc.innercircle.jinhoonboarding.survey.domain.Survey
import fc.innercircle.jinhoonboarding.survey.domain.QuestionType
import fc.innercircle.jinhoonboarding.survey.repository.SurveyRepository
import org.springframework.stereotype.Service

@Service
class SurveyService(
    private val surveyRepository: SurveyRepository
) {

    // 설문조사 생성
    fun createSurvey(request: CreateSurveyRequest): Survey {
        val surveyToCreate = request.toDomain()
        return surveyRepository.save(surveyToCreate)
    }
}