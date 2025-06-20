package com.survey.dto

import java.util.UUID

/**
 * request, response 바디용 DTO
 */

/**
 * 설문 생성 요청 DTO
 */
data class SurveyCreateRequest(
    val title: String,
    val description: String? = null
)

/**
 * 설문 항목 생성/수정 요청 DTO
 */
data class SurveyItemRequest(
    val surveyId: UUID,
    val question: String,
    val type: String,
    val itemOrder: Int,
    val options: List<SurveyOptionRequest>? = null
)

/**
 * 설문 항목 옵션 DTO
 */
data class SurveyOptionRequest(
    val id: UUID? = null,
    val optionValue: String,
    val optionOrder: Int,
    val active: Boolean = true  // true → 표시, false → 비활성(삭제 또는 수정된)
)

/**
 * 설문 응답 요청 DTO
 */
data class ResponseRequest(
    val surveyId: UUID,
    val respondent: String? = null,
    val responseItems: List<ResponseItemRequest>
)

/**
 * 설문 응답 항목 요청 DTO
 */
data class ResponseItemRequest(
    val surveyItemId: UUID,
    val answer: String? = null,
    val optionIds: List<UUID> = emptyList()
)