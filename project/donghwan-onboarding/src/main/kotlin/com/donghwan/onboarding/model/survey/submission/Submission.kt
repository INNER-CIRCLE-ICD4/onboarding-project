package com.donghwan.onboarding.model.survey.submission

import java.time.LocalDateTime

data class Submission(
    override val submissionId: Long,
    override val surveyId: Long,
    override val surveyVersionId: Long,
    override val content: Map<String, Any>,
    override val submittedAt: LocalDateTime,
): SubmissionModel

