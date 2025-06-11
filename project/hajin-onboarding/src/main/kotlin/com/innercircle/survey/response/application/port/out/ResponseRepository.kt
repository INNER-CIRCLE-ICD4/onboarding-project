package com.innercircle.survey.response.application.port.out

import com.innercircle.survey.response.domain.Response
import java.util.UUID

interface ResponseRepository {
    fun save(response: Response): Response

    fun findById(id: UUID): Response?

    fun findBySurveyId(surveyId: UUID): List<Response>
}
