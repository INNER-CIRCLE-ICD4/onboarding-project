package com.innercircle.survey.survey.application.port.out

import com.innercircle.survey.survey.domain.Survey

interface SaveSurveyPort {
    fun save(survey: Survey): Survey
}
