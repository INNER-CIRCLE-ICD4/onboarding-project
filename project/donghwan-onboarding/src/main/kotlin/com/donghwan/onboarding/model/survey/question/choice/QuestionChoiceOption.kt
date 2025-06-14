package com.donghwan.onboarding.model.survey.question.choice

data class QuestionChoiceOption(
    override val questionItemId: Long,
    override val questionChoiceOptionId: Long,
    override val order: Int,
    override val content: String,
) : QuestionChoiceOptionModel
