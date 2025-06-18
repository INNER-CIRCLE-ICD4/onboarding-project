package com.wlghsp.querybox.ui.dto

import com.wlghsp.querybox.domain.survey.Option
import com.wlghsp.querybox.domain.survey.Options
import com.wlghsp.querybox.domain.survey.Question
import com.wlghsp.querybox.domain.survey.QuestionType
import com.wlghsp.querybox.domain.survey.Questions
import com.wlghsp.querybox.domain.survey.Survey

data class SurveyCreateRequest(
    val title: String,
    val description: String,
    val questions: List<QuestionRequest>
) {
    fun toEntity(): Survey {
        val questionEntities = questions.map { it.toEntity() }
        return Survey.of(
            title = title,
            description = description,
            questions = Questions.of(questionEntities.toMutableList())
        )
    }
}

data class QuestionRequest(
    val name: String,
    val description: String,
    val type: QuestionType,
    val required: Boolean,
    val options: List<String>?
) {
    fun toEntity(): Question {
        val optionVOs = options?.map { Option(it) } ?: emptyList()
        val optionsWrapped = if (type.isChoice()) Options(optionVOs) else null
        return Question(name, description, type, required, optionsWrapped)
    }
}