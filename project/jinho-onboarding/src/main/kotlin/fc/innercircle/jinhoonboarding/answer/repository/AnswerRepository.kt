package fc.innercircle.jinhoonboarding.answer.repository

import fc.innercircle.jinhoonboarding.answer.domain.AnswerSet
import org.springframework.data.jpa.repository.JpaRepository

interface AnswerRepository: JpaRepository<AnswerSet, Long> {

}
