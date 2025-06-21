package com.wlghsp.querybox.domain.response

import com.wlghsp.querybox.domain.survey.Survey

data class SurveySnapshotPayload(
    val survey: Survey,
    val answers: Answers,
)