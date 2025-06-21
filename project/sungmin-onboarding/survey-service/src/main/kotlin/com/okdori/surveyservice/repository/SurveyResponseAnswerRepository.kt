package com.okdori.surveyservice.repository

import com.okdori.surveyservice.domain.SurveyResponseAnswer
import org.springframework.data.jpa.repository.JpaRepository

/**
 * packageName    : com.okdori.surveyservice.repository
 * fileName       : SurveyResponseAnswerRepository
 * author         : okdori
 * date           : 2025. 6. 18.
 * description    :
 */
interface SurveyResponseAnswerRepository: JpaRepository<SurveyResponseAnswer, String> {
}
