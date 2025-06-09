package com.innercircle.survey.survey.adapter.out.persistence

import com.innercircle.survey.survey.application.port.out.SurveyRepository
import com.innercircle.survey.survey.domain.Survey
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class SurveyPersistenceAdapter(
    private val surveyJpaRepository: SurveyJpaRepository,
) : SurveyRepository {
    override fun save(survey: Survey): Survey {
        return surveyJpaRepository.save(survey)
    }

    override fun findById(id: UUID): Survey? {
        return surveyJpaRepository.findByIdWithQuestions(id)
    }

    override fun findAll(): List<Survey> {
        return surveyJpaRepository.findAll()
    }

    override fun deleteById(id: UUID) {
        surveyJpaRepository.deleteById(id)
    }

    override fun existsById(id: UUID): Boolean {
        return surveyJpaRepository.existsById(id)
    }
}
