package onboardingproject.project

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class SoojinhanOnboardingApplication

fun main(args: Array<String>) {
    runApplication<SoojinhanOnboardingApplication>(*args)
}
