package fc.innercircle.jinhoonboarding.survey.dto

data class CreateSurveyRequest(
    val title: String,
    val description: String,
    val questions: List<QuestionDTO>
)
