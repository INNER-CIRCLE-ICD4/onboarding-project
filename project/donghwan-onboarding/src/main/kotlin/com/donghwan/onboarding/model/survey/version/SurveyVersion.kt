package com.donghwan.onboarding.model.survey.version

import java.time.LocalDateTime

data class SurveyVersion(
    override val surveyId: Long,
    override val surveyVersionId: Long,
    override val sequence: Int,
    override val title: String,
    override val description: String,
    override val createdAt: LocalDateTime
):SurveyVersionModel
