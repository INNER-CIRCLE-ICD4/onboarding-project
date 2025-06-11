package com.innercircle.survey.survey.adapter.out.persistence

import com.innercircle.survey.survey.application.port.out.SurveyRepository
import com.innercircle.survey.survey.domain.Survey
import mu.KotlinLogging
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import java.util.UUID

private val logger = KotlinLogging.logger {}

@Component
class SurveyPersistenceAdapter(
    private val surveyJpaRepository: SurveyJpaRepository,
) : SurveyRepository {
    override fun save(survey: Survey): Survey {
        logger.debug { "Saving survey: ${survey.title}" }
        return surveyJpaRepository.save(survey)
    }

    override fun findById(surveyId: UUID): Survey? {
        logger.debug { "Loading survey by id: $surveyId" }
        // Fetch Join을 사용하여 N+1 문제 해결
        return surveyJpaRepository.findByIdWithQuestionsAndChoices(surveyId)
    }

    override fun findAll(pageable: Pageable): Page<Survey> {
        logger.debug { "Loading all surveys with pagination: page=${pageable.pageNumber}, size=${pageable.pageSize}" }
        // 대량 데이터 처리를 위한 페이징 적용
        return surveyJpaRepository.findAllWithPaging(pageable)
    }
}
