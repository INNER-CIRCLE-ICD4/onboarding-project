package com.wlghsp.querybox.domain.response

import com.wlghsp.querybox.domain.survey.QuestionType

object AnswerValidator {
    fun validate(questionType: QuestionType, answerValue: String?, selectedOptionIds: List<Long>?) {
        when (questionType) {
            QuestionType.SHORT_TEXT, QuestionType.LONG_TEXT -> {
                require(!answerValue.isNullOrBlank()) { "주관식 응답은 비어 있을 수 없습니다." }
            }
            QuestionType.SINGLE_CHOICE -> {
                require(!selectedOptionIds.isNullOrEmpty()) { "선택형 응답은 하나 이상 선택해야 합니다." }
            }

           QuestionType.MULTIPLE_CHOICE -> {
                require(!selectedOptionIds.isNullOrEmpty()) { "선택형 응답은 하나 이상 선택해야 합니다." }
            }
        }
    }
}