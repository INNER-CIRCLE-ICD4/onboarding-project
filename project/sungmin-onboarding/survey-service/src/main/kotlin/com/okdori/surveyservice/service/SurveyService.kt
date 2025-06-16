package com.okdori.surveyservice.service

import com.okdori.surveyservice.domain.Survey
import com.okdori.surveyservice.domain.SurveyItem
import com.okdori.surveyservice.domain.SurveyItemOption
import com.okdori.surveyservice.dto.SurveyCreateDto
import com.okdori.surveyservice.exception.BadRequestException
import com.okdori.surveyservice.exception.ErrorCode.*
import com.okdori.surveyservice.repository.SurveyItemOptionRepository
import com.okdori.surveyservice.repository.SurveyItemRepository
import com.okdori.surveyservice.repository.SurveyRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * packageName    : com.okdori.surveyservice.service
 * fileName       : SurveyService
 * author         : okdori
 * date           : 2025. 6. 16.
 * description    :
 */

@Service
@Transactional
class SurveyService(
    private val surveyRepository: SurveyRepository,
    private val surveyItemRepository: SurveyItemRepository,
    private val surveyItemOptionRepository: SurveyItemOptionRepository,
) {
    private fun existSurveyName(surveyName: String)
        = surveyRepository.existsBySurveyName(surveyName)

    fun createSurvey(surveyCreateDto: SurveyCreateDto): Survey {
        if (existSurveyName(surveyCreateDto.surveyName)) {
            throw BadRequestException(SURVEY_NAME_DUPLICATED)
        }

        val survey = surveyRepository.save(
            Survey().apply {
                surveyName = surveyCreateDto.surveyName
                surveyDescription = surveyCreateDto.surveyDescription
            }
        )

        val surveyItems = surveyCreateDto.items.map { item ->
            SurveyItem(survey).apply {
                itemName = item.itemName
                itemDescription = item.itemDescription
                itemType = item.itemType
                isRequired = item.isRequired
                hasOtherOption = item.hasOtherOption
            }
        }

        val savedItems = surveyItemRepository.saveAll(surveyItems)

        val allOptions = savedItems.flatMapIndexed { index, savedItem ->
            surveyCreateDto.items[index].options.map { option ->
                SurveyItemOption(savedItem).apply {
                    optionName = option.optionName
                }
            }
        }

        if (allOptions.isNotEmpty()) {
            surveyItemOptionRepository.saveAll(allOptions)
        }

        return survey
    }
}
