package com.donghwan.onboarding.model.survey.submission.search

data class SubmissionSearch(
    override val submissionSearchId: Long,
    override val surveyId: Long,
    override val surveyVersionId: Long,
    override val submissionId: Long,
    override val contentOrigin: ContentOrigin,
    override val contentValue: String
) : SubmissionSearchModel