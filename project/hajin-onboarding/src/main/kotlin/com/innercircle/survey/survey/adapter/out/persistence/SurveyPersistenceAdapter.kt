package com.innercircle.survey.survey.adapter.out.persistence

import com.innercircle.survey.survey.application.port.out.SaveSurveyPort
import com.innercircle.survey.survey.domain.Survey
import org.springframework.stereotype.Repository

@Repository
class SurveyPersistenceAdapter(
    private val surveyJpaRepository: SurveyJpaRepository,
) : SaveSurveyPort {
    override fun save(survey: Survey): Survey {
        return surveyJpaRepository.save(survey)
    }
}
