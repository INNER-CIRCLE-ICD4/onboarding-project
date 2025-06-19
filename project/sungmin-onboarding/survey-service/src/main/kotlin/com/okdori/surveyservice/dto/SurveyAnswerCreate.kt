package com.okdori.surveyservice.dto

import com.okdori.surveyservice.domain.SurveyItem

/**
 * packageName    : com.okdori.surveyservice.dto
 * fileName       : SurveyAnswerCreate
 * author         : okdori
 * date           : 2025. 6. 18.
 * description    :
 */
data class SurveyAnswerCreateDto(
    val responseUser: String? = null,
    val answers: List<AnswerCreateDto>,
)

data class AnswerCreateDto(
    val itemId: String,
    val answer: String?,
    val options: List<String> = emptyList(),
    val otherValue: String?,
)

data class SurveyItemWithOptions(
    val surveyItem: SurveyItem,
    val options: List<String>,
)

data class NormalizedAnswer(
    val itemId: String,
    val values: List<String>,
)
