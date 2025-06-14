package com.donghwan.onboarding.model.survey.submission

import com.donghwan.onboarding.model.survey.SurveyIdentity
import com.donghwan.onboarding.model.survey.version.SurveyVersionIdentity
import com.donghwan.onboarding.model.time.SubmittedTimeProps

interface SubmissionProps {
    val content: Map<String, Any>
}

interface SubmissionModel :
    SubmissionIdentity,
    SurveyIdentity,
    SurveyVersionIdentity,
    SubmissionProps,
    SubmittedTimeProps