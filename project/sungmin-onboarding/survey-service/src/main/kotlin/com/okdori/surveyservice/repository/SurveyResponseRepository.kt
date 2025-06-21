package com.okdori.surveyservice.repository

import com.okdori.surveyservice.domain.SurveyResponse
import org.springframework.data.jpa.repository.JpaRepository

/**
 * packageName    : com.okdori.surveyservice.repository
 * fileName       : SurveyResponseRepository
 * author         : okdori
 * date           : 2025. 6. 18.
 * description    :
 */
interface SurveyResponseRepository: JpaRepository<SurveyResponse, String> {
}
