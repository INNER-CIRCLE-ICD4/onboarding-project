package com.okdori.surveyservice.service

import com.okdori.surveyservice.domain.ItemType
import com.okdori.surveyservice.domain.QSurveyItem.surveyItem
import com.okdori.surveyservice.domain.QSurveyItemOption.surveyItemOption
import com.okdori.surveyservice.domain.QSurveyResponse.surveyResponse
import com.okdori.surveyservice.domain.QSurveyResponseAnswer.surveyResponseAnswer
import com.okdori.surveyservice.domain.Survey
import com.okdori.surveyservice.domain.SurveyItem
import com.okdori.surveyservice.domain.SurveyItemOption
import com.okdori.surveyservice.domain.SurveyResponse
import com.okdori.surveyservice.domain.SurveyResponseAnswer
import com.okdori.surveyservice.dto.AnswerCreateDto
import com.okdori.surveyservice.dto.AnswerResponseDto
import com.okdori.surveyservice.dto.ItemResponseDto
import com.okdori.surveyservice.dto.NormalizedAnswer
import com.okdori.surveyservice.dto.PagedResponse
import com.okdori.surveyservice.dto.ResponseSearchDto
import com.okdori.surveyservice.dto.SurveyCreateDto
import com.okdori.surveyservice.dto.SurveyAnswerCreateDto
import com.okdori.surveyservice.dto.SurveyAnswerResponseDto
import com.okdori.surveyservice.dto.SurveyItemWithOptions
import com.okdori.surveyservice.dto.SurveyResponseDto
import com.okdori.surveyservice.exception.BadRequestException
import com.okdori.surveyservice.exception.ErrorCode
import com.okdori.surveyservice.exception.ErrorCode.*
import com.okdori.surveyservice.exception.NotFoundException
import com.okdori.surveyservice.repository.SearchCondition
import com.okdori.surveyservice.repository.SurveyItemOptionRepository
import com.okdori.surveyservice.repository.SurveyItemRepository
import com.okdori.surveyservice.repository.SurveyRepository
import com.okdori.surveyservice.repository.SurveyResponseAnswerRepository
import com.okdori.surveyservice.repository.SurveyResponseRepository
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.collections.List

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
    private val queryFactory: JPAQueryFactory,
    private val searchCondition: SearchCondition,

    private val surveyRepository: SurveyRepository,
    private val surveyItemRepository: SurveyItemRepository,
    private val surveyItemOptionRepository: SurveyItemOptionRepository,
    private val surveyResponseRepository: SurveyResponseRepository,
    private val surveyResponseAnswerRepository: SurveyResponseAnswerRepository,
) {
    private fun existSurveyName(surveyName: String) = surveyRepository.existsBySurveyName(surveyName)

    private fun findSurveyById(surveyId: String) =
        surveyRepository.findByIdOrNull(surveyId) ?: throw NotFoundException(NOT_FOUND_SURVEY)

    private fun findSurveyItemsBySurvey(survey: Survey) = surveyItemRepository.findBySurveyAndIsDeleted(survey, false)

    private fun findOptionNamesBySurveyItem(surveyItem: SurveyItem): List<String> =
        surveyItemOptionRepository.findBySurveyItemAndIsDeleted(surveyItem, false)
            .map { it.optionName }

    private fun toSurveyItemWithOptionsMap(surveyItems: List<SurveyItem>): Map<String, SurveyItemWithOptions> {
        return surveyItems.associateBy(
            keySelector = { it.id!! },
            valueTransform = { surveyItem ->
                SurveyItemWithOptions(
                    surveyItem = surveyItem,
                    options = findOptionNamesBySurveyItem(surveyItem)
                )
            }
        )
    }

    fun createSurvey(surveyCreateDto: SurveyCreateDto): SurveyResponseDto {
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

        return getSurvey(survey.id!!)
    }

    @Transactional(readOnly = true)
    fun getSurvey(surveyId: String): SurveyResponseDto {
        findSurveyById(surveyId).let { survey ->
            val itemOptionsMap = queryFactory
                .select(
                    surveyItem,
                    surveyItemOption
                )
                .from(surveyItem)
                .leftJoin(surveyItemOption).on(surveyItem.id.eq(surveyItemOption.surveyItem.id)).fetchJoin()
                .where(
                    surveyItem.survey.id.eq(surveyId)
                        .and(surveyItem.isDeleted.eq(false))
                        .and(surveyItemOption.isDeleted.eq(false).or(surveyItemOption.isNull))
                )
                .orderBy(surveyItem.id.asc())
                .fetch()
                .groupBy({ it.get(surveyItem)!! }) { tuple ->
                    tuple.get(surveyItemOption)
                }

            return SurveyResponseDto(
                survey,
                itemOptionsMap.map { (item, options) ->
                    ItemResponseDto(item, options.filterNotNull())
                }
            )
        }
    }

    fun createSurveyResponse(surveyId: String, surveyAnswerCreateDto: SurveyAnswerCreateDto): SurveyAnswerResponseDto {
        findSurveyById(surveyId).let { survey ->
            val surveyResponse = surveyResponseRepository.save(
                SurveyResponse(survey).apply {
                    responseUser = surveyAnswerCreateDto.responseUser
                }
            )

            val surveyItemsMap = toSurveyItemWithOptionsMap(findSurveyItemsBySurvey(survey))
            val normalizedAnswers = surveyAnswerCreateDto.answers.map { answer ->
                NormalizedAnswer(
                    itemId = answer.itemId,
                    values = normalizeAnswerValues(answer, surveyItemsMap[answer.itemId])
                )
            }
            validateAnswers(normalizedAnswers, surveyItemsMap)

            val answerResponse = surveyResponseAnswerRepository.saveAll(
                normalizedAnswers.map { (itemId, values) ->
                    val surveyItem = surveyItemsMap[itemId]!!.surveyItem
                    SurveyResponseAnswer(surveyResponse, surveyItem).apply {
                        itemName = surveyItem.itemName
                        answer = values.toString()
                    }
                }
            )

            return SurveyAnswerResponseDto(surveyResponse).apply {
                answers = answerResponse.map(::AnswerResponseDto)
            }
        }
    }

    private fun normalizeAnswerValues(answer: AnswerCreateDto, itemWithOptions: SurveyItemWithOptions?): List<String> {
        return when (itemWithOptions?.surveyItem?.itemType) {
            ItemType.SHORT_TEXT, ItemType.LONG_TEXT -> {
                listOfNotNull(answer.answer?.takeIf { it.isNotBlank() })
            }

            ItemType.SINGLE_SELECT, ItemType.MULTIPLE_SELECT -> {
                val selectedOptions = answer.options.filter { it.isNotBlank() }
                val otherValue = answer.otherValue?.takeIf { it.isNotBlank() }?.let { "기타: $it" }
                selectedOptions + listOfNotNull(otherValue)
            }

            else -> throw BadRequestException(NOT_FOUND_SURVEY_ITEM)
        }
    }

    private fun validateAnswers(normalizedAnswers: List<NormalizedAnswer>, surveyItemsMap: Map<String, SurveyItemWithOptions>) {
        val answeredItemIds = normalizedAnswers.map { it.itemId }

        validateNoDuplicateItems(answeredItemIds)
        validateRequiredItems(surveyItemsMap, answeredItemIds)

        normalizedAnswers.forEach { answer ->
            val itemWithOptions = surveyItemsMap[answer.itemId]
                ?: throw NotFoundException(NOT_FOUND_SURVEY_ITEM)
            validateNormalizedAnswer(answer, itemWithOptions)
        }
    }

    private fun validateNoDuplicateItems(itemIds: List<String>) {
        require(itemIds.size == itemIds.distinct().size) { throw BadRequestException(RESPONSE_DUPLICATED) }
    }

    private fun validateRequiredItems(surveyItemsMap: Map<String, SurveyItemWithOptions>, answeredItemIds: List<String>) {
        val missingRequiredItems = surveyItemsMap.values
            .filter { it.surveyItem.isRequired && it.surveyItem.id !in answeredItemIds }
            .map { it.surveyItem.id }

        require(missingRequiredItems.isEmpty()) {
            throw BadRequestException(REQUIRED_RESPONSE_MISSING)
        }
    }

    private fun validateNormalizedAnswer(answer: NormalizedAnswer, itemWithOptions: SurveyItemWithOptions) {
        when (itemWithOptions.surveyItem.itemType) {
            ItemType.SHORT_TEXT -> validateTextAnswer(answer.values, 200, SHORT_TEXT_TOO_LONG)
            ItemType.LONG_TEXT -> validateTextAnswer(answer.values, 2000, LONG_TEXT_TOO_LONG)
            ItemType.SINGLE_SELECT -> validateSingleSelectAnswer(answer.values, itemWithOptions)
            ItemType.MULTIPLE_SELECT -> validateMultipleSelectAnswer(answer.values, itemWithOptions)
        }
    }

    private fun validateTextAnswer(values: List<String>, maxLength: Int, errorCode: ErrorCode) {
        require(values.size == 1) { throw BadRequestException(INVALID_RESPONSE_FORMAT) }
        require(values.first().length <= maxLength) { throw BadRequestException(errorCode) }
    }

    private fun validateSingleSelectAnswer(values: List<String>, itemWithOptions: SurveyItemWithOptions) {
        require(values.size == 1) { throw BadRequestException(INVALID_SINGLE_SELECT) }

        validateSelectAnswer(itemWithOptions.options, values.first(), itemWithOptions)
    }

    private fun validateMultipleSelectAnswer(values: List<String>, itemWithOptions: SurveyItemWithOptions) {
        require(values.isNotEmpty()) { throw BadRequestException(MULTIPLE_SELECT_REQUIRED) }
        require(values.size == values.distinct().size) { throw BadRequestException(INVALID_MULTIPLE_SELECT) }

        values.forEach { selectedValue ->
            validateSelectAnswer(itemWithOptions.options, selectedValue, itemWithOptions)
        }
    }

    private fun validateSelectAnswer(availableOptions: List<String>, selectedValue: String, itemWithOptions: SurveyItemWithOptions) {
        val isValidOption = availableOptions.contains(selectedValue)
        val isOtherValue = selectedValue.startsWith("기타: ") && itemWithOptions.surveyItem.hasOtherOption

        require(isValidOption || isOtherValue) {
            throw BadRequestException(INVALID_OPTION_NAME)
        }
    }

    @Transactional(readOnly = true)
    fun getSurveyResponse(
        surveyId: String,
        itemName: String?,
        answerValue: String?,
        responseUser: String?,
        page: Int = 0,
        size: Int = 20,
    ): PagedResponse<ResponseSearchDto> {
        val responseIds = queryFactory
            .select(surveyResponse.id)
            .from(surveyResponse)
            .where(
                surveyResponse.survey.id.eq(surveyId),
                searchCondition.responseUser(responseUser),
                searchCondition.answerCondition(itemName, answerValue)
            )
            .orderBy(surveyResponse.id.desc())
            .offset((page * size).toLong())
            .limit(size.toLong())
            .fetch()

        if (responseIds.isEmpty()) return PagedResponse.empty()

        val results = queryFactory
            .select(
                surveyResponse.id,
                surveyResponse.responseUser,
                surveyResponse.createdDate,
                surveyResponseAnswer.id,
                surveyResponseAnswer.itemName,
                surveyResponseAnswer.answer
            )
            .from(surveyResponse)
            .leftJoin(surveyResponseAnswer).on(surveyResponse.id.eq(surveyResponseAnswer.surveyResponse.id))
            .where(surveyResponse.id.`in`(responseIds))
            .orderBy(surveyResponse.id.desc())
            .fetch()

        val content = results
            .groupBy { it.get(surveyResponse.id)!! }
            .map { (responseId, tuples) ->
                val first = tuples.first()
                ResponseSearchDto(
                    responseId = responseId,
                    responseUser = first.get(surveyResponse.responseUser),
                    createdDate = first.get(surveyResponse.createdDate)!!,
                    answers = tuples.mapNotNull { tuple ->
                        tuple.get(surveyResponseAnswer.id)?.let {
                            AnswerResponseDto(
                                answerId = it,
                                itemName = tuple.get(surveyResponseAnswer.itemName)!!,
                                answer = tuple.get(surveyResponseAnswer.answer)!!
                            )
                        }
                    }
                )
            }

        val totalCount = queryFactory
            .select(surveyResponse.count())
            .from(surveyResponse)
            .where(
                surveyResponse.survey.id.eq(surveyId),
                searchCondition.responseUser(responseUser),
                searchCondition.answerCondition(itemName, answerValue)
            )
            .fetchOne() ?: 0L

        return PagedResponse(
            content = content,
            page = page,
            size = size,
            totalElements = totalCount,
            totalPages = (totalCount + size - 1) / size,
            last = page >= (totalCount + size - 1) / size - 1
        )
    }
}
