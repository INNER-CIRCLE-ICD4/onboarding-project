package com.okdori.surveyservice.dto

import com.okdori.surveyservice.domain.ItemType
import com.okdori.surveyservice.domain.Survey
import com.okdori.surveyservice.domain.SurveyItem
import com.okdori.surveyservice.domain.SurveyItemOption

/**
 * packageName    : com.okdori.surveyservice.dto
 * fileName       : SurveyResponse
 * author         : okdori
 * date           : 2025. 6. 17.
 * description    :
 */
data class SurveyResponseDto(
    var surveyId: String,
    var surveyName: String,
    var surveyDescription: String?,
    val items: List<ItemResponseDto> = emptyList(),
) {
    constructor(survey: Survey, itemResponses: List<ItemResponseDto>) : this(
        surveyId = survey.id!!,
        surveyName = survey.surveyName,
        surveyDescription = survey.surveyDescription,
        items = itemResponses
    )
}

data class ItemResponseDto(
    var itemId: String,
    var itemName: String,
    var itemDescription: String?,
    var itemType: ItemType,
    var isRequired: Boolean,
    var hasOtherOption: Boolean,
    val options: List<OptionResponseDto> = emptyList(),
) {
    constructor(item: SurveyItem, options: List<SurveyItemOption>) : this(
        itemId = item.id!!,
        itemName = item.itemName,
        itemDescription = item.itemDescription,
        itemType = item.itemType,
        isRequired = item.isRequired,
        hasOtherOption = item.hasOtherOption,
        options = options.map { OptionResponseDto(it) }
    )
}

data class OptionResponseDto(
    var optionId: String,
    var optionName: String,
) {
    constructor(option: SurveyItemOption) : this(
        optionId = option.id!!,
        optionName = option.optionName,
    )
}
