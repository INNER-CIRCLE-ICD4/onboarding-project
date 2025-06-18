package com.wlghsp.querybox.ui.dto

import com.wlghsp.querybox.domain.survey.QuestionType

data class SnapshotDto(
    val survey: SurveySnapshotDto,
    val response: ResponseSnapshotDto
)

data class SurveySnapshotDto(
    val title: String,
    val description: String,
    val questions: List<QuestionSnapshotDto>
)

data class QuestionSnapshotDto(
    val name: String,
    val description: String,
    val type: QuestionType,
    val required: Boolean,
    val options: List<String>?
)

data class ResponseSnapshotDto(
    val answers: List<AnswerSnapshotDto>
)

data class AnswerSnapshotDto(
    val questionName: String,
    val answerValue: String
)