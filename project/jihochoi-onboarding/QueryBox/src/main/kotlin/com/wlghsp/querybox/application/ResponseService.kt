package com.wlghsp.querybox.application

import com.wlghsp.querybox.domain.response.SnapshotFactory
import com.wlghsp.querybox.domain.survey.Survey
import com.wlghsp.querybox.repository.ResponseRepository
import com.wlghsp.querybox.repository.SurveyRepository
import com.wlghsp.querybox.ui.dto.ResponseCreateRequest
import com.wlghsp.querybox.ui.dto.ResponseDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class ResponseService(
    private val responseRepository: ResponseRepository,
    private val surveyRepository: SurveyRepository,
    private val snapshotFactory: SnapshotFactory,
    private val responseValidatorService: ResponseValidatorService,
) {
    fun submit(surveyId: Long, request: ResponseCreateRequest) {
        val survey = findSurvey(surveyId)

        responseValidatorService.validateAnswers(survey.questions, request.answers)

        val answers = request.getAnswers()
        val snapshot = snapshotFactory.createSnapshot(survey, answers)

        responseRepository.save(survey.createResponse(answers, snapshot))
    }

    private fun findSurvey(surveyId: Long): Survey = (surveyRepository.findSurveyWithQuestionsById(surveyId)
        ?: throw IllegalArgumentException("해당 ID의 설문이 존재하지 않습니다: $surveyId"))

    @Transactional(readOnly = true)
    fun searchResponses(surveyId: Long, questionKeyword: String?, answerKeyword: String?): List<ResponseDto> {
        val responses = responseRepository.findAllBySurveyId(surveyId)
        return responses.flatMap { it.toFilteredDtos(questionKeyword, answerKeyword) }
    }

}