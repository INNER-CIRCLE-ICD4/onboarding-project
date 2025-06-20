package kr.innercircle.onboarding.survey.repository

import kr.innercircle.onboarding.survey.domain.Survey
import kr.innercircle.onboarding.survey.domain.SurveyItem
import org.springframework.data.jpa.repository.JpaRepository

/**
 * packageName : kr.innercircle.onboarding.survey.repository
 * fileName    : SurveyItemJpaRepository
 * author      : ckr
 * date        : 25. 6. 16.
 * description :
 */
interface SurveyItemJpaRepository: JpaRepository<SurveyItem, Long> {
    fun findAllBySurveyAndDeleted(survey: Survey, deleted: Boolean): List<SurveyItem>
}