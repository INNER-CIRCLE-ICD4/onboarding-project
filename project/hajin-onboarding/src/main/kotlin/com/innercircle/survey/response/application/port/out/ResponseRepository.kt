package com.innercircle.survey.response.application.port.out

import com.innercircle.survey.response.adapter.out.persistence.dto.ResponseSummaryProjection
import com.innercircle.survey.response.application.port.`in`.ResponseUseCase.ResponseSearchCriteria
import com.innercircle.survey.response.domain.Response
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface ResponseRepository {
    fun save(response: Response): Response

    fun findById(id: UUID): Response?

    fun findBySurveyId(surveyId: UUID): List<Response>

    fun findBySurveyIdWithAnswers(
        surveyId: UUID,
        pageable: Pageable,
    ): Page<Response>

    fun findResponseSummariesBySurveyId(
        surveyId: UUID,
        pageable: Pageable,
    ): Page<ResponseSummaryProjection>

    fun searchResponsesByCriteria(
        criteria: ResponseSearchCriteria,
        pageable: Pageable,
    ): Page<Response>
}
