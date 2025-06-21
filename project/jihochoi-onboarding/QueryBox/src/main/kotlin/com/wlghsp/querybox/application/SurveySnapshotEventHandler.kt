package com.wlghsp.querybox.application

import com.fasterxml.jackson.databind.ObjectMapper
import com.wlghsp.querybox.domain.response.ResponseSubmittedEvent
import com.wlghsp.querybox.domain.response.ResponseSnapshot
import com.wlghsp.querybox.domain.response.SurveySnapshotPayload
import com.wlghsp.querybox.repository.SurveyRepository
import com.wlghsp.querybox.repository.SurveySnapshotRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.event.TransactionalEventListener

@Component
class SurveySnapshotEventHandler(
    private val surveyRepository: SurveyRepository,
    private val snapshotRepository: SurveySnapshotRepository,
    private val objectMapper: ObjectMapper
) {
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener
    fun handle(event: ResponseSubmittedEvent) {
        val survey = surveyRepository.findSurveyWithQuestionsById(event.surveyId)
            ?: throw IllegalStateException("설문 없음: ${event.surveyId}")

        val payload = SurveySnapshotPayload(
            survey = survey,
            answers = event.answers,
        )

        val snapshotJson = objectMapper.writeValueAsString(payload)

        val snapshot = ResponseSnapshot(
            id = event.responseId,
            surveyId = event.surveyId,
            snapshot = snapshotJson
        )
        snapshotRepository.save(snapshot)
    }
}