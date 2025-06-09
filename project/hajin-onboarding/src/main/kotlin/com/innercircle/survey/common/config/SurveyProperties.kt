package com.innercircle.survey.common.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.survey")
data class SurveyProperties(
    val maxItemsPerSurvey: Int = 10,
    val maxChoicesPerItem: Int = 20,
    val response: ResponseProperties = ResponseProperties(),
) {
    data class ResponseProperties(
        val maxTextLength: Int = 1000,
    )
}
