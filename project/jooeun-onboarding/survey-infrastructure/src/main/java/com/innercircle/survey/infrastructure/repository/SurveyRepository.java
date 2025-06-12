package com.innercircle.survey.infrastructure.repository;

import com.innercircle.survey.domain.survey.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 설문조사 엔티티에 대한 데이터 액세스 인터페이스
 */
@Repository
public interface SurveyRepository extends JpaRepository<Survey, String> {

    /**
     * 설문조사 ID로 조회 (활성화된 질문들과 함께)
     *
     * @param surveyId 설문조사 ID
     * @return 설문조사
     */
    @Query("SELECT s FROM Survey s " +
           "LEFT JOIN FETCH s.questions q " +
           "WHERE s.id = :surveyId " +
           "ORDER BY q.displayOrder ASC")
    Optional<Survey> findByIdWithQuestions(@Param("surveyId") String surveyId);

    /**
     * 설문조사 ID로 조회 (활성화된 질문들만)
     *
     * @param surveyId 설문조사 ID
     * @return 설문조사
     */
    @Query("SELECT s FROM Survey s " +
           "LEFT JOIN FETCH s.questions q " +
           "WHERE s.id = :surveyId AND q.active = true " +
           "ORDER BY q.displayOrder ASC")
    Optional<Survey> findByIdWithActiveQuestions(@Param("surveyId") String surveyId);

    /**
     * 설문조사가 존재하는지 확인
     *
     * @param surveyId 설문조사 ID
     * @return 존재 여부
     */
    boolean existsById(String surveyId);
}
