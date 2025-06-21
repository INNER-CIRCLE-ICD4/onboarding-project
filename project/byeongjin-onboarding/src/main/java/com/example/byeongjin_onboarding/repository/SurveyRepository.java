package com.example.byeongjin_onboarding.repository;

import com.example.byeongjin_onboarding.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long> {
}