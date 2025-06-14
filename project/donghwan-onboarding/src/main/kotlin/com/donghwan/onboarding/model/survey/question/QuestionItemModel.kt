package com.donghwan.onboarding.model.survey.question

import com.donghwan.onboarding.model.survey.question.QuestionType
import com.donghwan.onboarding.model.survey.version.SurveyVersionIdentity

interface QuestionItemProps {
    val required: Boolean
    val order: Int
    val title: String
    val description: String
    val questionType: QuestionType
}

interface QuestionItemModel : SurveyVersionIdentity, QuestionItemIdentity, QuestionItemProps {
}