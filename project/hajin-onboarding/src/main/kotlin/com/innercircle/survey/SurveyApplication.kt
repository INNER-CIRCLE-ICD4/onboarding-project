package com.innercircle.survey

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class SurveyApplication

fun main(args: Array<String>) {
    runApplication<SurveyApplication>(*args)
}
