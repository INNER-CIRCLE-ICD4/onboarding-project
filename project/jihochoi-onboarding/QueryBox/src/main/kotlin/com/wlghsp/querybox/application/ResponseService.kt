package com.wlghsp.querybox.application

import com.wlghsp.querybox.domain.response.Answer
import com.wlghsp.querybox.repository.ResponseRepository
import com.wlghsp.querybox.repository.SurveyRepository
import com.wlghsp.querybox.ui.dto.ResponseDto
import com.wlghsp.querybox.ui.dto.ResponseCreateRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class ResponseService(
    private val responseRepository: ResponseRepository,
    private val surveyRepository: SurveyRepository,
) {
    fun submit(surveyId: Long, request: ResponseCreateRequest) {
        val survey = surveyRepository.findSurveyWithQuestionsById(surveyId)

        val answers = request.answers.map {
            Answer.of(
                questionId = it.questionId,
                questionName = it.questionName,
                questionType = it.questionType,
                answerValue = it.answerValue,
                selectedOptionIds = it.selectedIds,
            )
        }
    }

    fun findAllBySurveyId(surveyId: Long): List<ResponseDto> {
        TODO("Not yet implemented")
    }

}