package com.innercircle.survey.response.adapter.out.persistence.dto

import java.time.LocalDateTime
import java.util.UUID

data class ResponseSummaryProjection(
    val id: UUID,
    val surveyId: UUID,
    val surveyVersion: Int,
    val respondentId: String?,
    val createdAt: LocalDateTime,
    val answerCount: Int,
)
