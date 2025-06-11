package com.innercircle.survey.response.adapter.out.persistence

import com.innercircle.survey.response.domain.Response
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID

interface ResponseJpaRepository : JpaRepository<Response, UUID> {
    @Query(
        """
        SELECT DISTINCT r FROM Response r
        LEFT JOIN FETCH r._answers a
        LEFT JOIN FETCH a.question
        WHERE r.survey.id = :surveyId
    """,
    )
    fun findBySurveyId(
        @Param("surveyId") surveyId: UUID,
        pageable: Pageable,
    ): Page<Response>

    @Query(
        """
        SELECT DISTINCT r FROM Response r
        LEFT JOIN FETCH r._answers a
        LEFT JOIN FETCH a.question
        WHERE r.survey.id = :surveyId
    """,
    )
    fun findAllBySurveyId(
        @Param("surveyId") surveyId: UUID,
    ): List<Response>

    fun countBySurveyId(surveyId: UUID): Long
}
