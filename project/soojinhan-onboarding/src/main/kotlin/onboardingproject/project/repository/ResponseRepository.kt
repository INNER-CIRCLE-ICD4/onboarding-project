package onboardingproject.project.repository

import onboardingproject.project.domain.Response
import org.springframework.data.jpa.repository.JpaRepository

/**
 * packageName : onboardingproject.project.repository
 * fileName    : ResponseRepository
 * author      : hsj
 * date        : 2025. 6. 14.
 * description :
 */
interface ResponseRepository : JpaRepository<Response, String>