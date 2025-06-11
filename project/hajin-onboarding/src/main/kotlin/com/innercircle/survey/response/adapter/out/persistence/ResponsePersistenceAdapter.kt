package com.innercircle.survey.response.adapter.out.persistence

import com.innercircle.survey.response.application.port.out.ResponseRepository
import com.innercircle.survey.response.domain.Response
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
        return responseJpaRepository.findByIdWithAnswers(id)
    }

    override fun findBySurveyId(surveyId: UUID): List<Response> {
        return responseJpaRepository.findBySurveyId(surveyId)
    }
}
