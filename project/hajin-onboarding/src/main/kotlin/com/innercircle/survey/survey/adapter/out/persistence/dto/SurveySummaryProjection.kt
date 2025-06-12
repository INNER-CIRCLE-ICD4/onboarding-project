package com.innercircle.survey.survey.adapter.out.persistence.dto

import java.time.LocalDateTime
import java.util.UUID

data class SurveySummaryProjection(
    val id: UUID,
    val title: String,
    val description: String,
    val version: Int,
    val createdAt: LocalDateTime,
    val questionCount: Int,
)
