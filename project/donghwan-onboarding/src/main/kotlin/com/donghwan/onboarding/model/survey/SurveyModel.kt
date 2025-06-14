package com.donghwan.onboarding.model.survey

import com.donghwan.onboarding.model.time.CreateTimeProps
import com.donghwan.onboarding.model.time.UpdatedTimeProps

interface SurveyModel : SurveyIdentity, CreateTimeProps, UpdatedTimeProps {
}