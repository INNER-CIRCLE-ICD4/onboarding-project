package com.wlghsp.querybox.application

import com.wlghsp.querybox.domain.survey.Survey
import com.wlghsp.querybox.repository.SurveyRepository
import com.wlghsp.querybox.ui.dto.SurveyCreateRequest
import com.wlghsp.querybox.ui.dto.SurveyUpdateRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class SurveyService(
    private val surveyRepository: SurveyRepository,
) {

    fun create(request: SurveyCreateRequest): Long {
        val saved = surveyRepository.save(request.toEntity())
        return saved.id
    }

    fun update(surveyId: Long, request: SurveyUpdateRequest) {
        val survey = findSurveyWithQuestionsById(surveyId)
        survey.update(request)
    }
    @Transactional(readOnly = true)
    fun findSurveyWithQuestionsById(id: Long): Survey {
        return surveyRepository.findSurveyWithQuestionsById(id)
            ?: throw IllegalArgumentException("해당 ID의 설문이 존재하지 않습니다: $id")
    }

}