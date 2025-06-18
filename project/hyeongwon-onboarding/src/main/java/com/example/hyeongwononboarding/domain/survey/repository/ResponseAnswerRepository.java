package com.example.hyeongwononboarding.domain.survey.repository;

import com.example.hyeongwononboarding.domain.survey.entity.ResponseAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 설문 응답 상세 Repository
 */
public interface ResponseAnswerRepository extends JpaRepository<ResponseAnswer, String> {
}
