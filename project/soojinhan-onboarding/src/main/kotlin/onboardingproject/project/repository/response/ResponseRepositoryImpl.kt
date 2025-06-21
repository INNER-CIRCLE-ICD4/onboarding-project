package onboardingproject.project.repository.response

import onboardingproject.project.domain.Response
import org.springframework.stereotype.Repository

/**
 * packageName : onboardingproject.project.repository.response
 * fileName    : ResponseRepositoryImpl
 * author      : hsj
 * date        : 2025. 6. 20.
 * description :
 */
@Repository
class ResponseRepositoryImpl(
    private val responseJpaRepository: ResponseJpaRepository
) : ResponseRepository {
    override fun save(response: Response): Response {
        return responseJpaRepository.save(response)
    }

    override fun deleteAll() {
        responseJpaRepository.deleteAll()
    }

    override fun findAllBySurveyFieldIdIn(fieldIds: List<String>): List<Response> {
        return responseJpaRepository.findAllBySurveyFieldIdIn(fieldIds)
    }
}