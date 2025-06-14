package com.example.hyeongwononboarding.domain.survey.repository;

import com.example.hyeongwononboarding.domain.survey.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 설문조사 저장소(JPA Repository)
 * Survey 엔티티의 CRUD를 담당합니다.
 */
@Repository
public interface SurveyRepository extends JpaRepository<Survey, String> {
}
