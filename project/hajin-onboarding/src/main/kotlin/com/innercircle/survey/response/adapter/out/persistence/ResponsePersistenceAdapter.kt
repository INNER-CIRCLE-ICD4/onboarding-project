package com.innercircle.survey.response.adapter.out.persistence

import com.innercircle.survey.response.adapter.out.persistence.dto.ResponseSummaryProjection
import com.innercircle.survey.response.application.port.`in`.ResponseUseCase.ResponseSearchCriteria
import com.innercircle.survey.response.application.port.out.ResponseRepository
import com.innercircle.survey.response.domain.Response
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class ResponsePersistenceAdapter(
    private val responseJpaRepository: ResponseJpaRepository,
) : ResponseRepository {
    override fun save(response: Response): Response {
        return responseJpaRepository.save(response)
    }

    override fun findById(id: UUID): Response? {
        return responseJpaRepository.findByIdWithGraph(id)
    }

    override fun findBySurveyId(surveyId: UUID): List<Response> {
        return responseJpaRepository.findBySurveyId(surveyId)
    }

    override fun findBySurveyIdWithAnswers(
        surveyId: UUID,
        pageable: Pageable,
    ): Page<Response> {
        return responseJpaRepository.findBySurveyIdWithAnswers(surveyId, pageable)
    }

    override fun findResponseSummariesBySurveyId(
        surveyId: UUID,
        pageable: Pageable,
    ): Page<ResponseSummaryProjection> {
        return responseJpaRepository.findResponseSummariesBySurveyId(surveyId, pageable)
    }

    override fun searchResponsesByCriteria(
        criteria: ResponseSearchCriteria,
        pageable: Pageable,
    ): Page<Response> {
        return responseJpaRepository.searchResponsesByCriteria(
            surveyId = criteria.surveyId,
            questionTitle = criteria.questionTitle,
            answerValue = criteria.answerValue,
            pageable = pageable,
        )
    }
}
