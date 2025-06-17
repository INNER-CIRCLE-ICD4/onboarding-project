package fc.innercircle.jinhoonboarding.survey.dto.response

import fc.innercircle.jinhoonboarding.survey.domain.Survey

data class SurveyResponse(
    val title: String,
    val description: String,
    val questions: List<QuestionResponse>
){
    data class QuestionResponse (
        val title: String,
        val description: String,
        val questionType: String,
        val required: Boolean = false,
        val options: List<String> = emptyList()
    )

    companion object {
        fun fromDomain(response: Survey): SurveyResponse {
            val questions = response.questions.map {
                QuestionResponse(
                    title = it.title,
                    description = it.description,
                    questionType = it.questionType.name,
                    required = it.required,
                    options = it.options
                )
            }.toList()

            val surveyResponse = SurveyResponse(
                title = response.title,
                description = response.description,
                questions = questions
            )

            return surveyResponse
        }
    }

}