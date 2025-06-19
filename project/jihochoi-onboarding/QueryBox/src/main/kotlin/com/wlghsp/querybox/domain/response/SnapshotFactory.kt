package com.wlghsp.querybox.domain.response

import com.fasterxml.jackson.databind.ObjectMapper
import com.wlghsp.querybox.domain.survey.Survey
import com.wlghsp.querybox.ui.dto.ResponseSnapshotDto
import com.wlghsp.querybox.ui.dto.SnapshotDto
import com.wlghsp.querybox.ui.dto.SurveySnapshotDto
import org.springframework.stereotype.Component

@Component
class SnapshotFactory(
    private val objectMapper: ObjectMapper
) {
    fun createSnapshot(survey: Survey, answers: Answers): String {
        val snapshot = SnapshotDto(
            survey = SurveySnapshotDto.from(survey),
            response = ResponseSnapshotDto.from(answers.values())
        )

        return objectMapper.writeValueAsString(snapshot)
    }
}