package com.example.hyeongwononboarding.domain.survey.repository;

import com.example.hyeongwononboarding.domain.survey.entity.SurveyVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 설문조사 버전 저장소(JPA Repository)
 * SurveyVersion 엔티티의 CRUD 및 버전별 조회를 담당합니다.
 */
@Repository
public interface SurveyVersionRepository extends JpaRepository<SurveyVersion, String> {
    Optional<SurveyVersion> findBySurveyIdAndVersion(String surveyId, Integer version);
}
