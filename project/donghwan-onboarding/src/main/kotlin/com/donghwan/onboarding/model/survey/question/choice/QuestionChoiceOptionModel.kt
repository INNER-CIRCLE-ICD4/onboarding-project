package com.donghwan.onboarding.model.survey.question.choice

interface QuestionChoiceOptionProps {
    val order: Int
    val content: String
}

interface QuestionChoiceOptionModel : QuestionChoiceOptionProps {
}
