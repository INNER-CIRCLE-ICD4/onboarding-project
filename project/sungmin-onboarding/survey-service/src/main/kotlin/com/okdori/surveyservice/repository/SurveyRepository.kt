package com.okdori.surveyservice.repository

import com.okdori.surveyservice.domain.Survey
import org.springframework.data.jpa.repository.JpaRepository

/**
 * packageName    : com.okdori.surveyservice.repository
 * fileName       : SurveyRepository
 * author         : okdori
 * date           : 2025. 6. 16.
 * description    :
 */
interface SurveyRepository: JpaRepository<Survey, String> {
    fun existsBySurveyName(surveyName: String): Boolean
}
