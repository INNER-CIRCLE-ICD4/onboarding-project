package fc.innercircle.jinhoonboarding.repository

import fc.innercircle.jinhoonboarding.entity.Survey
import org.springframework.data.jpa.repository.JpaRepository

interface SurveyRepository: JpaRepository<Survey, Long> {

}