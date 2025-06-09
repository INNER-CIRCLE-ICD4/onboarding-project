package com.innercircle.survey.response.adapter.out.persistence

import com.innercircle.survey.response.domain.Response
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID

/**
 * Spring Data JPA 응답 리포지토리
 */
interface ResponseJpaRepository : JpaRepository<Response, UUID> {
    /**
     * 설문조사 ID로 응답 목록 조회 (페이징)
     */
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

    /**
     * 설문조사 ID로 모든 응답 조회
     */
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

    /**
     * 설문조사 ID로 응답 수 조회
     */
    fun countBySurveyId(surveyId: UUID): Long
}
