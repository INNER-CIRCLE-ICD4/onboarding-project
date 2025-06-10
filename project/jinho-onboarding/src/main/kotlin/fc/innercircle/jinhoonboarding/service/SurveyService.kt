package fc.innercircle.jinhoonboarding.service

import fc.innercircle.jinhoonboarding.dto.CreateSurveyRequest
import fc.innercircle.jinhoonboarding.dto.QuestionDTO
import fc.innercircle.jinhoonboarding.entity.Question
import fc.innercircle.jinhoonboarding.entity.Survey
import fc.innercircle.jinhoonboarding.enum.QuestionType
import fc.innercircle.jinhoonboarding.repository.SurveyRepository
import org.springframework.stereotype.Service

@Service
class SurveyService(
    private val surveyRepository: SurveyRepository
) {

    // 설문조사 생성
    fun createSurvey(request: CreateSurveyRequest): Survey {
        val newSurvey = Survey(
            title = request.title,
            description = request.description,
            questions = mutableListOf()
        )
        request.questions.map {
            if ((it.questionType == QuestionType.SINGLE_SELECT || it.questionType == QuestionType.MULTI_SELECT) && it.options.isNullOrEmpty()) {
                throw IllegalArgumentException("선택형 질문은 옵션값이 필수입니다.")
            }

            val question = Question (
                title = it.title,
                description = it.description,
                questionType = it.questionType,
                required = it.required,
                options = it.options,
                survey = newSurvey
            )
            newSurvey.questions.add(question)
        }
        return surveyRepository.save(newSurvey)
    }
}