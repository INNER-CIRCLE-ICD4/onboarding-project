package com.innercircle.survey.survey.adapter.`in`.web.dto

import java.time.LocalDateTime

data class SurveySummaryResponse(
    val id: String,
    val title: String,
    val description: String,
    val version: Int,
    val questionCount: Int,
    val createdAt: LocalDateTime,
)
