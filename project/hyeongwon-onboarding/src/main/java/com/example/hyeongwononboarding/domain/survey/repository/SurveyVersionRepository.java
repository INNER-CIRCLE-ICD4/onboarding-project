package com.example.hyeongwononboarding.domain.survey.repository;

import com.example.hyeongwononboarding.domain.survey.entity.SurveyVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.hyeongwononboarding.domain.survey.entity.Survey;
import java.util.Optional;

/**
 * 설문조사 버전 저장소(JPA Repository)
 * SurveyVersion 엔티티의 CRUD 및 버전별 조회를 담당합니다.
 */
@Repository
public interface SurveyVersionRepository extends JpaRepository<SurveyVersion, String> {
    Optional<SurveyVersion> findBySurveyIdAndVersionNumber(String surveyId, Integer versionNumber);
    
    /**
     * 설문의 최신 버전을 조회합니다.
     * @param survey 설문 엔티티
     * @return 최신 버전의 설문조사 버전 (없을 경우 Optional.empty())
     */
    Optional<SurveyVersion> findTopBySurveyOrderByVersionNumberDesc(Survey survey);
    
    /**
     * 설문 ID로 최신 버전을 조회합니다.
     * @param surveyId 설문 ID
     * @return 최신 버전의 설문조사 버전 (없을 경우 Optional.empty())
     */
    Optional<SurveyVersion> findTopBySurveyIdOrderByVersionNumberDesc(String surveyId);
}
