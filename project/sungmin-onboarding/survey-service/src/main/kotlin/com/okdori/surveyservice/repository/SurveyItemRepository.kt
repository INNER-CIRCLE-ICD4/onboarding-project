package com.okdori.surveyservice.repository

import com.okdori.surveyservice.domain.Survey
import com.okdori.surveyservice.domain.SurveyItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

/**
 * packageName    : com.okdori.surveyservice.repository
 * fileName       : SurveyItemRepository
 * author         : okdori
 * date           : 2025. 6. 16.
 * description    :
 */
interface SurveyItemRepository: JpaRepository<SurveyItem, String> {
    fun findBySurveyAndIsDeleted(survey: Survey, isDeleted: Boolean): List<SurveyItem>

    @Modifying
    @Query("UPDATE SurveyItem si SET si.isDeleted = true WHERE si.survey = :survey")
    fun softDeletedBySurvey(survey: Survey)
}
