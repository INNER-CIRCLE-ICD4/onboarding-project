package com.wlghsp.querybox.ui.dto

import com.wlghsp.querybox.domain.survey.QuestionType

data class SurveyUpdateRequest(
    val title: String,
    val description: String,
    val questions: List<QuestionUpdateRequest>
)

data class QuestionUpdateRequest(
    val id: Long?, // null이면 새로 추가되는 항목
    val name: String,
    val description: String,
    val type: QuestionType,
    val required: Boolean,
    val options: List<String>? // 단답/장문형은 null
)


