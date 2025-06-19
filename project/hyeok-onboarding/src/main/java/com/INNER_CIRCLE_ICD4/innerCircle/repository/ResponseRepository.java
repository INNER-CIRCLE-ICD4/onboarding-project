package com.INNER_CIRCLE_ICD4.innerCircle.repository;

import com.INNER_CIRCLE_ICD4.innerCircle.domain.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ResponseRepository extends JpaRepository<Response, UUID> {

    // 기본: 설문 ID로 응답 조회
    List<Response> findBySurveyId(UUID surveyId);

    // ✅ 응답 + 답변 + 선택지 까지 모두 페치 조인
    @Query("""
        SELECT DISTINCT r FROM Response r
        LEFT JOIN FETCH r.answers a
        LEFT JOIN FETCH a.selectedOptions
        WHERE r.survey.id = :surveyId
    """)
    List<Response> findBySurveyIdWithAnswersAndOptions(@Param("surveyId") UUID surveyId);
}
