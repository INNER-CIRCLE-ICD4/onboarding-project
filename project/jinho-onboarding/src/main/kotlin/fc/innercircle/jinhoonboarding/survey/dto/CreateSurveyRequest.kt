package fc.innercircle.jinhoonboarding.survey.dto

import fc.innercircle.jinhoonboarding.common.util.toEnumOrNull
import fc.innercircle.jinhoonboarding.survey.domain.Question
import fc.innercircle.jinhoonboarding.survey.domain.QuestionType
import fc.innercircle.jinhoonboarding.survey.domain.Survey
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size


data class CreateSurveyRequest(
    @field:NotBlank(message = "설문의 제목은 필수입니다.")
    val title: String,
    @field:NotBlank(message = "설문의 설명은 필수입니다.")
    val description: String,
    @field:Size(min = 1, max = 10, message = "질문항목은 최소 1개 이상, 최대 10개 이하여야 됩니다.")
    val questions: List<QuestionDTO>
){
    data class QuestionDTO (
        @field:NotBlank(message = "응답항목 제목은 필수입니다.")
        val title: String,
        @field:NotBlank(message = "응답항목 설명은 필수입니다.")
        val description: String,
        @field:NotBlank(message = "응답항목 타입은 필수입니다.")
        val questionType: String,
        val required: Boolean = false,
        @field:Size(max = 5, message = "선택항목은 최대 5까지만 가능합니다.")
        val options: List<String> = emptyList()
    )




    fun toDomain(): Survey {

        val newSurvey = Survey(
            title = title,
            description = description,
            questions = mutableListOf()
        )

        questions.map {
            val questionType = it.questionType.toEnumOrNull<QuestionType>() ?: throw RuntimeException("Invalid question type")

            if ((questionType == QuestionType.SINGLE_SELECT || questionType == QuestionType.MULTI_SELECT) && it.options.isEmpty()) {
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

        return newSurvey
    }
}
