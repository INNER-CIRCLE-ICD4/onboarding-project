package com.okdori.surveyservice.repository

import com.okdori.surveyservice.domain.SurveyItem
import com.okdori.surveyservice.domain.SurveyItemOption
import org.springframework.data.jpa.repository.JpaRepository

/**
 * packageName    : com.okdori.surveyservice.repository
 * fileName       : SurveyItemOptionRepository
 * author         : okdori
 * date           : 2025. 6. 16.
 * description    :
 */
interface SurveyItemOptionRepository: JpaRepository<SurveyItemOption, String> {
    fun findBySurveyItemAndIsDeleted(surveyItem: SurveyItem, isDeleted: Boolean): List<SurveyItemOption>
}
