package com.okdori.surveyservice.repository

import com.okdori.surveyservice.domain.Survey
import com.okdori.surveyservice.domain.SurveyItem
import org.springframework.data.jpa.repository.JpaRepository

/**
 * packageName    : com.okdori.surveyservice.repository
 * fileName       : SurveyItemRepository
 * author         : okdori
 * date           : 2025. 6. 16.
 * description    :
 */
interface SurveyItemRepository: JpaRepository<SurveyItem, String> {
    fun findBySurveyAndIsDeleted(survey: Survey, isDeleted: Boolean): List<SurveyItem>
}
