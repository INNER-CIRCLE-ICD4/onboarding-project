package com.example.hyeongwononboarding.domain.survey.repository;

import com.example.hyeongwononboarding.domain.survey.entity.QuestionOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 설문조사 질문 옵션 저장소(JPA Repository)
 * QuestionOption 엔티티의 CRUD를 담당합니다.
 */
@Repository
public interface QuestionOptionRepository extends JpaRepository<QuestionOption, String> {
}
