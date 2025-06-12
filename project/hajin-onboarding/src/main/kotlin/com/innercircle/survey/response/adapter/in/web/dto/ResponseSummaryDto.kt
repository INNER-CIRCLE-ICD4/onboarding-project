package com.innercircle.survey.response.adapter.`in`.web.dto

import java.time.LocalDateTime

data class ResponseSummaryDto(
    val id: String,
    val surveyId: String,
    val surveyVersion: Int,
    val respondentId: String?,
    val answerCount: Int,
    val createdAt: LocalDateTime,
)
