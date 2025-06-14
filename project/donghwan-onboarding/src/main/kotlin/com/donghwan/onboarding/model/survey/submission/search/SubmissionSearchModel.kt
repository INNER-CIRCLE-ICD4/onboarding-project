package com.donghwan.onboarding.model.survey.submission.search

import com.donghwan.onboarding.model.survey.SurveyIdentity
import com.donghwan.onboarding.model.survey.submission.SubmissionIdentity
import com.donghwan.onboarding.model.survey.version.SurveyVersionIdentity

interface SubmissionSearchProps {
    val contentOrigin: ContentOrigin
    val contentValue: String
}

interface SubmissionSearchModel :
    SubmissionSearchIdentity,
    SurveyIdentity,
    SurveyVersionIdentity,
    SubmissionIdentity,
    SubmissionSearchProps

