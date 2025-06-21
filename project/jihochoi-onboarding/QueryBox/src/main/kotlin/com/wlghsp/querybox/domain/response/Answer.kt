package com.wlghsp.querybox.domain.response

import com.wlghsp.querybox.domain.survey.QuestionType
import jakarta.persistence.Embeddable
import jakarta.persistence.Lob


@Embeddable
class Answer(
    val questionId: Long,
    val questionName: String,
    val questionType: QuestionType, // 항목 타입
    @Lob
    val answerValue: String? = null,
    val selectedOptionIds: List<Long>? = null,
    val selectedOptionTexts: List<String>? = null,
) {

    fun matchQuestion(questionKeyword: String?): Boolean {
        return questionKeyword.isNullOrBlank()
                || questionName.contains(questionKeyword, ignoreCase = true)
    }

    fun matchesAnswer(answerKeyword: String?): Boolean {
        return answerKeyword.isNullOrBlank()
                || (answerValue?.contains(answerKeyword, ignoreCase = true) == true
                || selectedOptionTexts?.any { it.contains(answerKeyword, ignoreCase = true) } == true)
    }

    companion object {
        fun of(
            questionId: Long,
            questionName: String,
            questionType: QuestionType,
            answerValue: String? = null,
            selectedOptionIds: List<Long>? = null,
            selectedOptionTexts: List<String>? = null,
        ): Answer {
            questionType.validateAnswer(answerValue, selectedOptionIds)
            return Answer(questionId, questionName, questionType, answerValue, selectedOptionIds, selectedOptionTexts)
        }
    }
}
