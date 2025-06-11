package com.innercircle.survey.survey.adapter.out.persistence

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
        // Fetch Join을 사용하여 N+1 문제 방지
        return surveyJpaRepository.findByIdWithQuestionsAndChoices(id)
    }

    override fun findAll(pageable: Pageable): Page<Survey> {
        // 목록 조회 시에는 Survey만 조회 (Lazy Loading)
        return surveyJpaRepository.findAllActive(pageable)
    }
}
