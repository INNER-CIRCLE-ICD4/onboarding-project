package kr.innercircle.onboarding.survey.repository

import kr.innercircle.onboarding.survey.domain.SurveyItem
import kr.innercircle.onboarding.survey.domain.SurveyItemOption
import org.springframework.data.jpa.repository.JpaRepository

/**
 * packageName : kr.innercircle.onboarding.survey.repository
 * fileName    : SurveyItemOptionJpaRepository
 * author      : ckr
 * date        : 25. 6. 17.
 * description :
 */
interface SurveyItemOptionJpaRepository: JpaRepository<SurveyItemOption, Long> {
    fun findAllBySurveyItemOrderByOrderNumber(surveyItem: SurveyItem): List<SurveyItemOption>
}