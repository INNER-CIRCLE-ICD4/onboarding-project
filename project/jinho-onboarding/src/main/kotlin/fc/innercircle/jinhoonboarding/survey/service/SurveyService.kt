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
        val newSurvey = Survey(
            title = request.title,
            description = request.description,
            questions = mutableListOf()
        )
        request.questions.map {
            val questionType = it.questionType.toEnumOrNull<QuestionType>() ?: throw RuntimeException("Invalid question type")
            if ((questionType == QuestionType.SINGLE_SELECT || questionType == QuestionType.MULTI_SELECT) && it.options.isNullOrEmpty()) {
                throw IllegalArgumentException("선택형 질문은 옵션값이 필수입니다.")
            }

            val question = Question (
                title = it.title,
                description = it.description,
                questionType = questionType,
                required = it.required,
                options = it.options,
                survey = newSurvey
            )
            newSurvey.questions.add(question)
        }
        return surveyRepository.save(newSurvey)
    }
}