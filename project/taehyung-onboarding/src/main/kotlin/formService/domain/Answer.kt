package formService.domain

import formService.exception.BadRequestException
import java.time.LocalDateTime

class Answer(
    val id: String,
    val userId: String,
    val submittedAt: LocalDateTime,
    val values: List<QuestionAnswer>,
) {
    fun validateAnswer(questions: List<Question>) {
        val map = HashMap<Long, QuestionAnswer>()

        values.forEach { map[it.questionId] = it }

        questions.forEach {
            // required 인데 answers 에서 누락되었다면 예외 처리
            if (it.required && map[it.id] == null) {
                throw BadRequestException("필수 항목 답변이 누락되었습니다.")
            } else if (it.inputType != map[it.id]?.answerType) {
                // questionId 의 타입이 answerType 과 다른 경우 예외 처리
                throw BadRequestException("답변 유형이 올바르지 않습니다.")
            }
        }
    }
}
