package fc.innercircle.jinhoonboarding.survey.repository

import fc.innercircle.jinhoonboarding.survey.domain.Survey
import org.springframework.data.jpa.repository.JpaRepository

interface SurveyRepository: JpaRepository<Survey, Long> {

}