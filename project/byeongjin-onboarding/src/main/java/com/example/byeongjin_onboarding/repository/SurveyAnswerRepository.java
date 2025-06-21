package com.example.byeongjin_onboarding.repository;

import com.example.byeongjin_onboarding.entity.SurveyAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyAnswerRepository extends JpaRepository<SurveyAnswer, Long> {
}