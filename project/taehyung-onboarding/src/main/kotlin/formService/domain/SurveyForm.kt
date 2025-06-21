package formService.domain

class SurveyForm(
    val id: String,
    var surveyName: String,
    var description: String,
    questions: List<Question>,
) {
    private val questionMutableList: MutableList<Question> = questions.toMutableList()
    val questions get() = questionMutableList.toList()

    init {
        if (questions.isEmpty()) {
            throw IllegalArgumentException("question size greater equal than 1")
        } else if (questions.size > 10) {
            throw IllegalArgumentException("question size less equal than 10")
        }
    }

    fun modify(
        surveyName: String,
        description: String,
    ) {
        this.surveyName = surveyName
        this.description = description
    }

    fun modifyQuestion(questions: List<Question>) {
        val map = HashMap<Long?, Question>()
        val mapQuestionOption = HashMap<Long?, QuestionOption>()

        this.questions.forEach { map[it.id] = it }

        questions.forEach {
            val findQuestion = map[it.id]

            findQuestion?.modify(
                name = it.name,
                description = it.description,
                inputType = it.inputType,
                required = it.required,
                isRemoved = it.isRemoved,
            )

            findQuestion?.options?.forEach { qo -> mapQuestionOption[qo.id] = qo }

            it.options?.forEach { qo ->
                val findQuestionOption = mapQuestionOption[qo.id]

                findQuestionOption?.modify(qo.value)

                mapQuestionOption.remove(qo.id)
            }

            map.remove(it.id)
        }
    }

    override fun toString(): String =
        """
        SurveyForm(
            id='$id', 
            surveyName='$surveyName', 
            description='$description', 
            questions=$questions
        )
        """.trimIndent()
}
