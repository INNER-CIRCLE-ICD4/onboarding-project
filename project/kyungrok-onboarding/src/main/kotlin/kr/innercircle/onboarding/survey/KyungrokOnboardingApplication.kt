package kr.innercircle.onboarding.survey

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class KyungrokOnboardingApplication

fun main(args: Array<String>) {
    runApplication<KyungrokOnboardingApplication>(*args)
}
