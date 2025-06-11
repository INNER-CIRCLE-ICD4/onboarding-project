package fc.innercircle.jinhoonboarding.survey.dto

data class QuestionDTO (
    val title: String,
    val description: String,
    val questionType: String,
    val required: Boolean,
    val options: List<String>?
)
