package com.donghwan.onboarding.model.survey.version

import com.donghwan.onboarding.model.time.CreateTimeProps

interface SurveyVersionProps {
    val surveyId: Long
    val title: String
    val description: String
    val sequence: Int
}

interface SurveyVersionModel : SurveyVersionIdentity, SurveyVersionProps, CreateTimeProps {
}