package com.okdori.surveyservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class SurveyServiceApplication

fun main(args: Array<String>) {
	runApplication<SurveyServiceApplication>(*args)
}
