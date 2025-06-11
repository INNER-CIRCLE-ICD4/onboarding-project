package com.innercircle.survey.response.adapter.out.persistence

import com.innercircle.survey.response.application.port.out.ResponseRepository
import com.innercircle.survey.response.domain.Response
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class ResponsePersistenceAdapter(
    private val responseJpaRepository: ResponseJpaRepository,
) : ResponseRepository {
    override fun save(response: Response): Response {
        return responseJpaRepository.save(response)
    }

    override fun findById(id: UUID): Response? {
        return responseJpaRepository.findById(id).orElse(null)
    }

    override fun findBySurveyId(
        surveyId: UUID,
        pageable: Pageable,
    ): Page<Response> {
        return responseJpaRepository.findBySurveyId(surveyId, pageable)
    }

    override fun findAllBySurveyId(surveyId: UUID): List<Response> {
        return responseJpaRepository.findAllBySurveyId(surveyId)
    }

    override fun deleteById(id: UUID) {
        responseJpaRepository.deleteById(id)
    }

    override fun countBySurveyId(surveyId: UUID): Long {
        return responseJpaRepository.countBySurveyId(surveyId)
    }
}
