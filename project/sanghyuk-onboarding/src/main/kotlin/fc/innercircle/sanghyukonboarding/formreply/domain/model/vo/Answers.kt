package fc.innercircle.sanghyukonboarding.formreply.domain.model.vo

import fc.innercircle.sanghyukonboarding.formreply.domain.model.Answer

class Answers(private val values: List<Answer> = emptyList()) {

    fun list(): List<Answer> {
        return values
    }

    fun getByQuestionId(questionId: String): Answer {
        return values.firstOrNull { it.questionId == questionId }
            ?: Answer.empty()
    }

    fun getValues(): List<Answer> {
        return values
    }

    fun isEmpty(): Boolean {
        return values.isEmpty()
    }

    fun size(): Int {
        return values.size
    }
}
