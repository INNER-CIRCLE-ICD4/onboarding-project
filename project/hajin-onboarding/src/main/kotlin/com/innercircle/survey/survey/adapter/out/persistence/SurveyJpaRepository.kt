package com.innercircle.survey.survey.adapter.out.persistence

import com.innercircle.survey.survey.adapter.out.persistence.dto.SurveySummaryProjection
import com.innercircle.survey.survey.domain.Survey
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
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

    @Query(
        value = """
        SELECT DISTINCT s FROM Survey s 
        LEFT JOIN FETCH s._questions q 
        WHERE s.isActive = true
        ORDER BY s.createdAt DESC
    """,
        countQuery = """
        SELECT COUNT(DISTINCT s) FROM Survey s 
        WHERE s.isActive = true
    """,
    )
    fun findAllActiveWithQuestions(pageable: Pageable): Page<Survey>

    @EntityGraph(attributePaths = ["_questions", "_questions._choices"])
    @Query("SELECT s FROM Survey s WHERE s.id = :id AND s.isActive = true")
    fun findByIdWithGraph(
        @Param("id") id: UUID,
    ): Survey?

    @Query(
        """
        SELECT new com.innercircle.survey.survey.adapter.out.persistence.dto.SurveySummaryProjection(
            s.id, s.title, s.description, s.version, s.createdAt, SIZE(s._questions)
        )
        FROM Survey s
        WHERE s.isActive = true
        ORDER BY s.createdAt DESC
    """,
    )
    fun findAllSurveySummaries(pageable: Pageable): Page<SurveySummaryProjection>
}
