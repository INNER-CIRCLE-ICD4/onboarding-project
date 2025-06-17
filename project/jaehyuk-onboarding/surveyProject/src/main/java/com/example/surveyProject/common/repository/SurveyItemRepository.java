package com.example.surveyProject.common.repository;

import com.example.surveyProject.entity.SurveyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Jpark
 * @date 2025-06-17
 * @Calss
 */
public interface SurveyItemRepository extends JpaRepository<SurveyEntity, Long> {}

