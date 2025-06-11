package formService.domain

class Question(
    var id: Long? = null,
    var name: String,
    var description: String,
    var inputType: QuestionInputType,
    var required: Boolean,
    var options: List<QuestionOption>? = null,
) {
    enum class QuestionInputType {
        SHORT_TEXT,
        LONG_TEXT,
        SINGLE_CHOICE,
        MULTI_CHOICE,
    }
}
