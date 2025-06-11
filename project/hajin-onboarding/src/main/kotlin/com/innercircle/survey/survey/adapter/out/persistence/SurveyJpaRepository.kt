package com.innercircle.survey.survey.adapter.out.persistence

import com.innercircle.survey.survey.domain.Survey
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface SurveyJpaRepository : JpaRepository<Survey, UUID> {
    @Query(
        """
        SELECT DISTINCT s FROM Survey s 
        LEFT JOIN FETCH s._questions q 
        LEFT JOIN FETCH q._choices 
        WHERE s.id = :id AND s.isActive = true
    """,
    )
    fun findByIdWithQuestionsAndChoices(
        @Param("id") id: UUID,
    ): Survey?

    @Query(
        """
        SELECT s FROM Survey s 
        WHERE s.isActive = true
        ORDER BY s.createdAt DESC
    """,
    )
    fun findAllActive(pageable: Pageable): Page<Survey>
}
