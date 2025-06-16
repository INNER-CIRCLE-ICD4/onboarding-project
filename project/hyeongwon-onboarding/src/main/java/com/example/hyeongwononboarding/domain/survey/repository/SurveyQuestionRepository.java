package com.example.hyeongwononboarding.domain.survey.repository;

import com.example.hyeongwononboarding.domain.survey.entity.SurveyQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.hyeongwononboarding.domain.survey.entity.SurveyVersion;
import java.util.List;

/**
 * 설문조사 질문 저장소(JPA Repository)
 * SurveyQuestion 엔티티의 CRUD 및 버전별 질문 목록 조회를 담당합니다.
 */
@Repository
public interface SurveyQuestionRepository extends JpaRepository<SurveyQuestion, String> {
    List<SurveyQuestion> findBySurveyVersionIdOrderByQuestionOrderAsc(String surveyVersionId);
    
    /**
     * 설문 버전으로 질문 목록을 조회합니다.
     * @param surveyVersion 설문 버전 엔티티
     * @return 질문 목록 (질문 순서대로 정렬)
     */
    List<SurveyQuestion> findBySurveyVersionOrderByQuestionOrderAsc(SurveyVersion surveyVersion);
}
