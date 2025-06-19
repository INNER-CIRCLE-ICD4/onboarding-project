package com.wlghsp.querybox.ui.dto

import com.wlghsp.querybox.domain.response.Answer
import com.wlghsp.querybox.domain.survey.Survey

data class SnapshotDto(
    val survey: SurveySnapshotDto,
    val response: ResponseSnapshotDto
)

data class SurveySnapshotDto(
    val title: String,
    val description: String
) {
    companion object {
        fun from(survey: Survey): SurveySnapshotDto =
            SurveySnapshotDto(
                title = survey.title,
                description = survey.description
            )
    }
}

data class ResponseSnapshotDto(
    val answers: List<AnswerSnapshotDto>
) {
    companion object {
        fun from(answers: List<Answer>): ResponseSnapshotDto =
            ResponseSnapshotDto(
                answers = answers.map {
                    AnswerSnapshotDto(
                        questionName = it.questionName,
                        answerValue = it.answerValue,
                        selectedOptionIds = it.selectedOptionIds ?: emptyList(),
                        selectedOptionTexts = it.selectedOptionTexts ?: emptyList()
                    )
                }
            )
    }
}

data class AnswerSnapshotDto(
    val questionName: String,
    val answerValue: String?,
    val selectedOptionIds: List<Long>,
    val selectedOptionTexts: List<String>,
)