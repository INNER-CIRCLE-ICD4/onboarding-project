package com.okdori.surveyservice.dto

import com.okdori.surveyservice.domain.SurveyResponse
import com.okdori.surveyservice.domain.SurveyResponseAnswer
import java.time.LocalDateTime

/**
 * packageName    : com.okdori.surveyservice.dto
 * fileName       : SurveyAnswerResponse
 * author         : okdori
 * date           : 2025. 6. 18.
 * description    :
 */
data class SurveyAnswerResponseDto(
    var responseId: String,
    var responseUser: String?,
    var createdDate: LocalDateTime,
    var answers: List<AnswerResponseDto> = emptyList(),
) {
    constructor(response: SurveyResponse) : this(
        responseId = response.id!!,
        responseUser = response.responseUser,
        createdDate = response.createdDate,
    )
}

data class AnswerResponseDto(
    var answerId: String,
    var itemName: String,
    var answer: String,
) {
    constructor(answer: SurveyResponseAnswer) : this(
        answerId = answer.id!!,
        itemName = answer.itemName,
        answer = answer.answer,
    )
}

data class ResponseSearchDto(
    var responseId: String,
    var responseUser: String?,
    var createdDate: LocalDateTime,
    var answers: List<AnswerResponseDto>,
)
