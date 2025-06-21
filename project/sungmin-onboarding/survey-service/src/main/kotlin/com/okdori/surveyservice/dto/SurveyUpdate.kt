package com.okdori.surveyservice.dto

import jakarta.validation.constraints.Size

/**
 * packageName    : com.okdori.surveyservice.dto
 * fileName       : SurveyUpdate
 * author         : okdori
 * date           : 2025. 6. 20.
 * description    :
 */
data class SurveyUpdateDto(
    @field:Size(max = 200)
    val surveyName: String?,
    @field:Size(max = 2000)
    val surveyDescription: String?,
    @field:Size(min = 1, max = 10)
    val items: List<ItemCreateDto>,
)
