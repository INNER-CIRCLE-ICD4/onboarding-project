package onboardingproject.project.repository.response

import onboardingproject.project.domain.Response
import org.springframework.data.jpa.repository.JpaRepository

/**
 * packageName : onboardingproject.project.repository
 * fileName    : ResponseJpaRepository
 * author      : hsj
 * date        : 2025. 6. 20.
 * description :
 */
interface ResponseJpaRepository: JpaRepository<Response, String> {
}