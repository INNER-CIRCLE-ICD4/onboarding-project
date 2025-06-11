package com.innercircle.survey.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.survey")
data class SurveyProperties(
    val maxItemsPerSurvey: Int = 10
)