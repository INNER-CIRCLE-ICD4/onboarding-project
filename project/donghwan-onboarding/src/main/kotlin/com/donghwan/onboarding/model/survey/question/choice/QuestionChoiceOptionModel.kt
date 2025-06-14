package com.donghwan.onboarding.model.survey.question.choice

import com.donghwan.onboarding.model.survey.question.QuestionItemIdentity

interface QuestionChoiceOptionProps {
    val order: Int
    val content: String
}

interface QuestionChoiceOptionModel : QuestionChoiceOptionIdentity, QuestionItemIdentity, QuestionChoiceOptionProps {
}
