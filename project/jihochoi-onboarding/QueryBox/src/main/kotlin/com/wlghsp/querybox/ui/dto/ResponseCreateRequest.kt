package com.wlghsp.querybox.ui.dto

import com.wlghsp.querybox.domain.survey.QuestionType

data class ResponseCreateRequest(
    val answers: List<AnswerRequest>
)

data class AnswerRequest(
    val questionId: Long,
    val questionName: String,
    val questionType: QuestionType,
    val answerValue: String,
    val selectedIds: List<Long>,
)
