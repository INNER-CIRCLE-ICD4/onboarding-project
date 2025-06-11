package com.innercircle.survey.response.adapter.out.persistence

import com.innercircle.survey.response.domain.Response
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ResponseJpaRepository : JpaRepository<Response, UUID> {
    @Query(
        """
        SELECT DISTINCT r FROM Response r 
        LEFT JOIN FETCH r._answers a 
        WHERE r.id = :id
    """,
    )
    fun findByIdWithAnswers(
        @Param("id") id: UUID,
    ): Response?

    @Query(
        """
        SELECT r FROM Response r 
        WHERE r.survey.id = :surveyId 
        ORDER BY r.createdAt DESC
    """,
    )
    fun findBySurveyId(
        @Param("surveyId") surveyId: UUID,
    ): List<Response>
}
