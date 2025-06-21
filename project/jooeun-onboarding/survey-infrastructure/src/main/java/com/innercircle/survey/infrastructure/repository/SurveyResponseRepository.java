package com.innercircle.survey.infrastructure.repository;

import com.innercircle.survey.domain.response.SurveyResponse;
import com.innercircle.survey.infrastructure.repository.custom.SurveyResponseSearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 설문조사 응답 엔티티에 대한 데이터 액세스 인터페이스
 */
@Repository
public interface SurveyResponseRepository extends JpaRepository<SurveyResponse, String>, SurveyResponseSearchRepository {

    /**
     * 설문조사 응답을 모든 답변과 함께 조회
     *
     * @param responseId 응답 ID
     * @return 설문조사 응답
     */
    @Query("SELECT sr FROM SurveyResponse sr " +
           "LEFT JOIN FETCH sr.answers a " +
           "WHERE sr.id = :responseId " +
           "ORDER BY a.createdAt ASC")
    Optional<SurveyResponse> findByIdWithAnswers(@Param("responseId") String responseId);

    /**
     * 특정 설문조사의 모든 응답 조회 (답변 포함)
     *
     * @param surveyId 설문조사 ID
     * @return 응답 목록
     */
    @Query("SELECT sr FROM SurveyResponse sr " +
           "LEFT JOIN FETCH sr.answers a " +
           "WHERE sr.survey.id = :surveyId " +
           "ORDER BY sr.createdAt DESC, a.createdAt ASC")
    List<SurveyResponse> findAllBySurveyIdWithAnswers(@Param("surveyId") String surveyId);

    /**
     * 특정 설문조사의 모든 응답 조회 (답변 제외, 요약만)
     *
     * @param surveyId 설문조사 ID
     * @return 응답 목록
     */
    @Query("SELECT sr FROM SurveyResponse sr " +
           "WHERE sr.survey.id = :surveyId " +
           "ORDER BY sr.createdAt DESC")
    List<SurveyResponse> findAllBySurveyId(@Param("surveyId") String surveyId);

    /**
     * 특정 설문조사의 응답 개수 조회
     *
     * @param surveyId 설문조사 ID
     * @return 응답 개수
     */
    @Query("SELECT COUNT(sr) FROM SurveyResponse sr WHERE sr.survey.id = :surveyId")
    long countBySurveyId(@Param("surveyId") String surveyId);

    /**
     * 특정 설문조사에 특정 응답자의 응답이 있는지 확인
     *
     * @param surveyId 설문조사 ID
     * @param respondentInfo 응답자 정보
     * @return 존재 여부
     */
    @Query("SELECT CASE WHEN COUNT(sr) > 0 THEN true ELSE false END " +
           "FROM SurveyResponse sr " +
           "WHERE sr.survey.id = :surveyId AND sr.respondentInfo = :respondentInfo")
    boolean existsBySurveyIdAndRespondentInfo(@Param("surveyId") String surveyId, 
                                             @Param("respondentInfo") String respondentInfo);
}
