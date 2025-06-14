package com.donghwan.onboarding.model.survey.question

import com.donghwan.onboarding.model.survey.question.choice.QuestionChoiceOptionModel
import com.donghwan.onboarding.model.survey.version.SurveyVersionIdentity

interface QuestionItemProps {
    val required: Boolean
    val order: Int
    val title: String
    val description: String
    val questionType: QuestionType
    val options: List<QuestionChoiceOptionModel>
}

interface QuestionItemModel :
    SurveyVersionIdentity,
    QuestionItemIdentity,
    QuestionItemProps