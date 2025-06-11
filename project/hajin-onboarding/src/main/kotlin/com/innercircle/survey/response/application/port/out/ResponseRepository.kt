package com.innercircle.survey.response.application.port.out

import com.innercircle.survey.response.domain.Response
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface ResponseRepository {
    fun save(response: Response): Response

    fun findById(id: UUID): Response?

    fun findBySurveyId(
        surveyId: UUID,
        pageable: Pageable,
    ): Page<Response>

    fun findAllBySurveyId(surveyId: UUID): List<Response>

    fun deleteById(id: UUID)

    fun countBySurveyId(surveyId: UUID): Long
}
