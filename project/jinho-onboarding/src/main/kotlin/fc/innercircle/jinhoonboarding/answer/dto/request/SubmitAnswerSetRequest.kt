package fc.innercircle.jinhoonboarding.answer.dto.request

data class SubmitAnswerSetRequest(

    val surveyId: Long,
    val userId: String,

    val answers: List<AnswerRequest>,
) {
    data class AnswerRequest(
        val question: QuestionData,
        val answer: String
    ) {
        data class QuestionData(
            val questionTitle: String,
            val questionDescription: String,
            val options: List<String>,
        )
    }

}
