package com.innercircle.survey.survey.adapter.out.persistence

import com.innercircle.survey.survey.adapter.out.persistence.dto.SurveySummaryProjection
import com.innercircle.survey.survey.application.port.out.SurveyRepository
import com.innercircle.survey.survey.domain.Survey
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class SurveyPersistenceAdapter(
    private val surveyJpaRepository: SurveyJpaRepository,
) : SurveyRepository {
    override fun save(survey: Survey): Survey {
        return surveyJpaRepository.save(survey)
    }

    override fun findById(id: UUID): Survey? {
        return surveyJpaRepository.findByIdWithGraph(id)
    }

    override fun findByIdWithFullDetails(surveyId: UUID): Survey? {
        return surveyJpaRepository.findByIdWithQuestionsAndChoices(surveyId)
    }

    override fun findAll(pageable: Pageable): Page<Survey> {
        return surveyJpaRepository.findAllActive(pageable)
    }

    override fun findAllWithQuestions(pageable: Pageable): Page<Survey> {
        return surveyJpaRepository.findAllActiveWithQuestions(pageable)
    }

    override fun findAllSummaries(pageable: Pageable): Page<SurveySummaryProjection> {
        return surveyJpaRepository.findAllSurveySummaries(pageable)
    }
}
