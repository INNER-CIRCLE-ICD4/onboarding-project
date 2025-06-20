package kr.innercircle.onboarding.survey.repository

import kr.innercircle.onboarding.survey.domain.Survey
import org.springframework.data.jpa.repository.JpaRepository

/**
 * packageName : kr.innercircle.onboarding.survey.repository
 * fileName    : SurveyJpaRepository
 * author      : ckr
 * date        : 25. 6. 16.
 * description :
 */
interface SurveyJpaRepository: JpaRepository<Survey, Long>