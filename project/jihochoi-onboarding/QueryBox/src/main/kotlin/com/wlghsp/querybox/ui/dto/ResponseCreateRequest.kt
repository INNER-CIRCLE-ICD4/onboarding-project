package com.wlghsp.querybox.ui.dto

import com.wlghsp.querybox.domain.response.Answer
import com.wlghsp.querybox.domain.response.Answers
import com.wlghsp.querybox.domain.survey.QuestionType

data class ResponseCreateRequest(
    val answers: List<AnswerRequest>
) {
    fun getAnswers(): Answers {
        return Answers.of(answers.map {
            Answer.of(
                questionId = it.questionId,
                questionName = it.questionName,
                questionType = it.questionType,
                answerValue = it.answerValue,
                selectedOptionIds = it.selectedIds,
                selectedOptionTexts = it.selectedOptionTexts
            )
        })
    }
}

data class AnswerRequest(
    val questionId: Long,
    val questionName: String,
    val questionType: QuestionType,
    val answerValue: String,
    val selectedIds: List<Long>,
    val selectedOptionTexts: List<String>,
)
