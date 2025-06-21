package com.innercircle.survey.infrastructure.repository.custom;

import com.innercircle.survey.domain.response.SurveyResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 설문조사 응답 고급 검색 Repository 구현체
 */
@Slf4j
@Repository
public class SurveyResponseSearchRepositoryImpl implements SurveyResponseSearchRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<SurveyResponse> searchResponses(String surveyId,
                                               String questionKeyword,
                                               String answerKeyword,
                                               String respondentKeyword,
                                               LocalDateTime startDate,
                                               LocalDateTime endDate) {
        
        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT DISTINCT sr FROM SurveyResponse sr ");
        jpql.append("LEFT JOIN FETCH sr.answers sa ");
        jpql.append("WHERE sr.survey.id = :surveyId ");

        List<String> parameters = new ArrayList<>();
        
        if (hasValue(questionKeyword) || hasValue(answerKeyword)) {
            jpql.append("AND EXISTS (");
            jpql.append("  SELECT 1 FROM SurveyAnswer sa2 ");
            jpql.append("  WHERE sa2.surveyResponse = sr ");
            
            if (hasValue(questionKeyword) && hasValue(answerKeyword)) {
                jpql.append("  AND (LOWER(sa2.questionTitle) LIKE LOWER(:questionKeyword) ");
                jpql.append("       OR EXISTS (SELECT 1 FROM sa2.answerValues av WHERE LOWER(av) LIKE LOWER(:answerKeyword))) ");
                parameters.add("questionKeyword");
                parameters.add("answerKeyword");
            } else if (hasValue(questionKeyword)) {
                jpql.append("  AND LOWER(sa2.questionTitle) LIKE LOWER(:questionKeyword) ");
                parameters.add("questionKeyword");
            } else {
                jpql.append("  AND EXISTS (SELECT 1 FROM sa2.answerValues av WHERE LOWER(av) LIKE LOWER(:answerKeyword)) ");
                parameters.add("answerKeyword");
            }
            jpql.append(") ");
        }

        if (hasValue(respondentKeyword)) {
            jpql.append("AND LOWER(sr.respondentInfo) LIKE LOWER(:respondentKeyword) ");
            parameters.add("respondentKeyword");
        }

        if (startDate != null) {
            jpql.append("AND sr.createdAt >= :startDate ");
            parameters.add("startDate");
        }

        if (endDate != null) {
            jpql.append("AND sr.createdAt <= :endDate ");
            parameters.add("endDate");
        }

        jpql.append("ORDER BY sr.createdAt DESC");

        TypedQuery<SurveyResponse> query = entityManager.createQuery(jpql.toString(), SurveyResponse.class);
        query.setParameter("surveyId", surveyId);

        if (parameters.contains("questionKeyword")) {
            query.setParameter("questionKeyword", "%" + questionKeyword + "%");
        }
        if (parameters.contains("answerKeyword")) {
            query.setParameter("answerKeyword", "%" + answerKeyword + "%");
        }
        if (parameters.contains("respondentKeyword")) {
            query.setParameter("respondentKeyword", "%" + respondentKeyword + "%");
        }
        if (parameters.contains("startDate")) {
            query.setParameter("startDate", startDate);
        }
        if (parameters.contains("endDate")) {
            query.setParameter("endDate", endDate);
        }

        List<SurveyResponse> result = query.getResultList();
        
        log.info("응답 검색 완료 - 조건: surveyId={}, questionKeyword={}, answerKeyword={}, respondentKeyword={}, " +
                "startDate={}, endDate={}, 결과 수: {}", 
                surveyId, questionKeyword, answerKeyword, respondentKeyword, startDate, endDate, result.size());
        
        return result;
    }

    @Override
    public long countSearchResponses(String surveyId,
                                    String questionKeyword,
                                    String answerKeyword,
                                    String respondentKeyword,
                                    LocalDateTime startDate,
                                    LocalDateTime endDate) {
        
        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT COUNT(DISTINCT sr) FROM SurveyResponse sr ");
        jpql.append("WHERE sr.survey.id = :surveyId ");

        List<String> parameters = new ArrayList<>();
        
        if (hasValue(questionKeyword) || hasValue(answerKeyword)) {
            jpql.append("AND EXISTS (");
            jpql.append("  SELECT 1 FROM SurveyAnswer sa ");
            jpql.append("  WHERE sa.surveyResponse = sr ");
            
            if (hasValue(questionKeyword) && hasValue(answerKeyword)) {
                jpql.append("  AND (LOWER(sa.questionTitle) LIKE LOWER(:questionKeyword) ");
                jpql.append("       OR EXISTS (SELECT 1 FROM sa.answerValues av WHERE LOWER(av) LIKE LOWER(:answerKeyword))) ");
                parameters.add("questionKeyword");
                parameters.add("answerKeyword");
            } else if (hasValue(questionKeyword)) {
                jpql.append("  AND LOWER(sa.questionTitle) LIKE LOWER(:questionKeyword) ");
                parameters.add("questionKeyword");
            } else {
                jpql.append("  AND EXISTS (SELECT 1 FROM sa.answerValues av WHERE LOWER(av) LIKE LOWER(:answerKeyword)) ");
                parameters.add("answerKeyword");
            }
            jpql.append(") ");
        }

        if (hasValue(respondentKeyword)) {
            jpql.append("AND LOWER(sr.respondentInfo) LIKE LOWER(:respondentKeyword) ");
            parameters.add("respondentKeyword");
        }

        if (startDate != null) {
            jpql.append("AND sr.createdAt >= :startDate ");
            parameters.add("startDate");
        }

        if (endDate != null) {
            jpql.append("AND sr.createdAt <= :endDate ");
            parameters.add("endDate");
        }

        TypedQuery<Long> query = entityManager.createQuery(jpql.toString(), Long.class);
        query.setParameter("surveyId", surveyId);

        if (parameters.contains("questionKeyword")) {
            query.setParameter("questionKeyword", "%" + questionKeyword + "%");
        }
        if (parameters.contains("answerKeyword")) {
            query.setParameter("answerKeyword", "%" + answerKeyword + "%");
        }
        if (parameters.contains("respondentKeyword")) {
            query.setParameter("respondentKeyword", "%" + respondentKeyword + "%");
        }
        if (parameters.contains("startDate")) {
            query.setParameter("startDate", startDate);
        }
        if (parameters.contains("endDate")) {
            query.setParameter("endDate", endDate);
        }

        return query.getSingleResult();
    }

    @Override
    public List<SurveyResponse> findAllResponsesForStatistics(String surveyId) {
        String jpql = """
            SELECT sr FROM SurveyResponse sr 
            LEFT JOIN FETCH sr.answers sa 
            WHERE sr.survey.id = :surveyId 
            ORDER BY sr.createdAt ASC
            """;

        TypedQuery<SurveyResponse> query = entityManager.createQuery(jpql, SurveyResponse.class);
        query.setParameter("surveyId", surveyId);

        return query.getResultList();
    }

    @Override
    public List<Object[]> getDailyResponseCounts(String surveyId, int days) {
        String jpql = """
            SELECT 
                DATE(sr.createdAt) as responseDate, 
                COUNT(sr) as responseCount 
            FROM SurveyResponse sr 
            WHERE sr.survey.id = :surveyId 
            AND sr.createdAt >= :sinceDate 
            GROUP BY DATE(sr.createdAt) 
            ORDER BY responseDate ASC
            """;

        TypedQuery<Object[]> query = entityManager.createQuery(jpql, Object[].class);
        query.setParameter("surveyId", surveyId);
        query.setParameter("sinceDate", LocalDateTime.now().minusDays(days));

        return query.getResultList();
    }

    /**
     * 문자열이 유효한 값을 가지는지 확인
     */
    private boolean hasValue(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
