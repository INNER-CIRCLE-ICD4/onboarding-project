package com.example.hyeongwononboarding.domain.survey.repository;

import com.example.hyeongwononboarding.domain.survey.entity.SurveyResponse;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 설문 응답 Repository
 */
public interface SurveyResponseRepository extends JpaRepository<SurveyResponse, String> {
}
