package com.example.surveyProject.common.repository;

import com.example.surveyProject.entity.SurveyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * @author Jpark
 * @date 2025-06-17
 * @Calss
 */
public interface SurveyRepository extends JpaRepository<SurveyEntity, Long> {



}

