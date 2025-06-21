package com.wlghsp.querybox.ui.dto

import com.wlghsp.querybox.domain.response.Answer
import com.wlghsp.querybox.domain.response.Response
import com.wlghsp.querybox.domain.survey.QuestionType

data class ResponseDto(
    val responseId: Long,
    val surveyId: Long,
    val questionId: Long,
    val questionName: String,
    val questionType: QuestionType,
    val answerValue: String?,
    val selectedOptionIds: List<Long>?,
    val selectedOptionTexts: List<String>?
) {
    companion object {
        fun from(response: Response, answer: Answer): ResponseDto {
            return ResponseDto(
                responseId = response.id,
                surveyId = response.surveyId,
                questionId = answer.questionId,
                questionName = answer.questionName,
                questionType = answer.questionType,
                answerValue = answer.answerValue,
                selectedOptionIds = answer.selectedOptionIds,
                selectedOptionTexts = answer.selectedOptionTexts,
            )
        }
    }
}
