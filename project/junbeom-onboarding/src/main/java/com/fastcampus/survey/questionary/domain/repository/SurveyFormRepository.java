package com.fastcampus.survey.questionary.domain.repository;

import java.util.Optional;

import com.fastcampus.survey.questionary.domain.model.SurveyForm;

public interface SurveyFormRepository {
    SurveyForm save(SurveyForm form);
    Optional<SurveyForm> findById(Long id);
    // 기타 필요한 메서드
} 