package com.okdori.surveyservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SurveyServiceApplication

fun main(args: Array<String>) {
	runApplication<SurveyServiceApplication>(*args)
}
