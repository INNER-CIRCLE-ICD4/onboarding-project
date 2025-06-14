package com.donghwan.onboarding.model.survey

import java.time.LocalDateTime

data class Survey(
    override val surveyId: Long,
    override val createdAt: LocalDateTime,
    override val updatedAt: LocalDateTime
) : SurveyModel
