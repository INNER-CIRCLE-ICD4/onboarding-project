package formService.domain

class SurveyForm(
    val id: String,
    var surveyName: String,
    var description: String,
    val questions: List<Question>,
) {
    init {
        if (questions.isEmpty()) {
            throw IllegalArgumentException("question size greater equal than 1")
        } else if (questions.size > 10) {
            throw IllegalArgumentException("question size less equal than 10")
        }
    }

    override fun toString(): String = "SurveyForm(id='$id', surveyName='$surveyName', description='$description', questions=$questions)"
}
