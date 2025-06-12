package com.innercircle.survey.survey.application.port.out

import com.innercircle.survey.survey.adapter.out.persistence.dto.SurveySummaryProjection
import com.innercircle.survey.survey.domain.Survey
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface SurveyRepository {
    fun save(survey: Survey): Survey

    fun findById(surveyId: UUID): Survey?

    fun findAll(pageable: Pageable): Page<Survey>

    fun findByIdWithFullDetails(surveyId: UUID): Survey?

    fun findAllWithQuestions(pageable: Pageable): Page<Survey>

    fun findAllSummaries(pageable: Pageable): Page<SurveySummaryProjection>
}
