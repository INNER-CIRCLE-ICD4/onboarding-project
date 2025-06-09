package com.innercircle.survey.survey.adapter.out.persistence

import com.innercircle.survey.survey.domain.Survey
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID

interface SurveyJpaRepository : JpaRepository<Survey, UUID> {
    @Query(
        """
        SELECT DISTINCT s FROM Survey s
        LEFT JOIN FETCH s._questions q
        LEFT JOIN FETCH q._choices
        WHERE s.id = :id
    """,
    )
    fun findByIdWithQuestions(
        @Param("id") id: UUID,
    ): Survey?
}
