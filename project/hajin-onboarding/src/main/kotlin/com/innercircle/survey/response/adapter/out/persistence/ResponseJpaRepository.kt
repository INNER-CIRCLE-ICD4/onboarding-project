package com.innercircle.survey.response.adapter.out.persistence

import com.innercircle.survey.response.adapter.out.persistence.dto.ResponseSummaryProjection
import com.innercircle.survey.response.domain.Response
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
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

    @EntityGraph(attributePaths = ["_answers", "survey"])
    @Query("SELECT r FROM Response r WHERE r.id = :id")
    fun findByIdWithGraph(
        @Param("id") id: UUID,
    ): Response?

    @Query(
        """
        SELECT DISTINCT r FROM Response r 
        LEFT JOIN FETCH r._answers a
        LEFT JOIN FETCH a.selectedChoiceIds
        LEFT JOIN FETCH a.selectedChoiceTexts
        WHERE r.survey.id = :surveyId 
        ORDER BY r.createdAt DESC
    """,
    )
    fun findBySurveyId(
        @Param("surveyId") surveyId: UUID,
    ): List<Response>

    @Query(
        value = """
        SELECT DISTINCT r FROM Response r 
        LEFT JOIN FETCH r._answers a
        LEFT JOIN FETCH a.selectedChoiceIds
        LEFT JOIN FETCH a.selectedChoiceTexts
        WHERE r.survey.id = :surveyId
        ORDER BY r.createdAt DESC
    """,
        countQuery = """
        SELECT COUNT(r) FROM Response r 
        WHERE r.survey.id = :surveyId
    """,
    )
    fun findBySurveyIdWithAnswers(
        @Param("surveyId") surveyId: UUID,
        pageable: Pageable,
    ): Page<Response>

    @Query(
        """
        SELECT new com.innercircle.survey.response.adapter.out.persistence.dto.ResponseSummaryProjection(
            r.id, r.survey.id, r.surveyVersion, r.respondentId, r.createdAt, SIZE(r._answers)
        )
        FROM Response r
        WHERE r.survey.id = :surveyId
        ORDER BY r.createdAt DESC
    """,
    )
    fun findResponseSummariesBySurveyId(
        @Param("surveyId") surveyId: UUID,
        pageable: Pageable,
    ): Page<ResponseSummaryProjection>


}
