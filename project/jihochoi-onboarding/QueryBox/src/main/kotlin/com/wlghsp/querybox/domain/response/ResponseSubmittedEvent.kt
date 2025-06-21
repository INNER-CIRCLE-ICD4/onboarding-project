package com.wlghsp.querybox.domain.response

data class ResponseSubmittedEvent(
    val responseId: Long,
    val surveyId: Long,
    val answers: Answers,
)