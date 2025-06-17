package com.okdori.surveyservice.dto

import com.okdori.surveyservice.domain.ItemType
import com.okdori.surveyservice.domain.ItemType.Companion.isSelectType
import com.okdori.surveyservice.exception.BadRequestException
import com.okdori.surveyservice.exception.ErrorCode
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

/**
 * packageName    : com.okdori.surveyservice.dto
 * fileName       : SurveyCreate
 * author         : okdori
 * date           : 2025. 6. 16.
 * description    :
 */
data class SurveyCreateDto(
    @field:NotBlank
    @field:Size(max = 200)
    val surveyName: String,
    @field:Size(max = 2000)
    val surveyDescription: String?,
    @field:Size(min = 1, max = 10)
    val items: List<ItemCreateDto>,
)

data class ItemCreateDto(
    @field:NotBlank
    @field:Size(max = 200)
    val itemName: String,
    @field:Size(max = 2000)
    val itemDescription: String?,
    val itemType: ItemType,
    val isRequired: Boolean = false,
    val hasOtherOption: Boolean = false,
    val options: List<OptionCreateDto> = emptyList(),
) {
    init {
        if (isSelectType(itemType)) {
            require(options.isNotEmpty()) {
                throw BadRequestException(ErrorCode.SELECT_OPTIONS_REQUIRED)
            }
        }
    }
}

data class OptionCreateDto(
    @field:NotBlank
    @field:Size(max = 200)
    val optionName: String,
)
