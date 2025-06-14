package com.donghwan.onboarding.model.survey.question

import com.donghwan.onboarding.model.survey.question.choice.QuestionChoiceOptionModel

data class QuestionItem(
    override val surveyVersionId: Long,
    override val questionItemId: Long,
    override val required: Boolean,
    override val order: Int,
    override val title: String,
    override val description: String,
    override val questionType: QuestionType,
    override val options: List<QuestionChoiceOptionModel>
): QuestionItemModel
