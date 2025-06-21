package onboardingproject.project.repository.response

import onboardingproject.project.domain.Response

/**
 * packageName : onboardingproject.project.repository.response
 * fileName    : ResponseRepository
 * author      : hsj
 * date        : 2025. 6. 20.
 * description :
 */
interface ResponseRepository {
    fun save(response: Response): Response
    fun deleteAll()
    fun findAllBySurveyFieldIdIn(fieldIds: List<String>): List<Response>
}