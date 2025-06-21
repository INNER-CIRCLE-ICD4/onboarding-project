package formService.domain

class QuestionAnswer(
    var id: Long? = null,
    val questionId: Long? = null,
    var answerValue: String,
    var answerType: InputType,
)
