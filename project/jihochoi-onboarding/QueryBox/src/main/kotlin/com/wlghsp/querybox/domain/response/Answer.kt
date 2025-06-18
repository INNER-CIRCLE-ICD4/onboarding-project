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
) {

    companion object {
        fun of(questionId: Long,
               questionName: String,
               questionType: QuestionType,
               answerValue: String? = null,
               selectedOptionIds: List<Long>? = null,
        ): Answer {
            AnswerValidator.validate(questionType, answerValue, selectedOptionIds)
            return Answer(questionId, questionName, questionType, answerValue, selectedOptionIds)
        }
    }
}