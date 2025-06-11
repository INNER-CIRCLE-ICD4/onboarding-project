package com.fastcampus.survey.questionary.adapter.out.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fastcampus.survey.questionary.adapter.out.entity.SurveyFormJpaEntity;

public interface SurveyFormJpaRepository extends JpaRepository<SurveyFormJpaEntity, Long> {
} 