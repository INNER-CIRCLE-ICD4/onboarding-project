package fc.innercircle.jinhoonboarding.dto

data class CreateSurveyRequest(
    val title: String,
    val description: String,
    val questions: List<QuestionDTO>
)
