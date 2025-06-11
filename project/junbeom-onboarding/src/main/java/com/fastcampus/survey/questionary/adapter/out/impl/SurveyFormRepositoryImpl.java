package com.fastcampus.survey.questionary.adapter.out.impl;

import com.fastcampus.survey.questionary.domain.model.SurveyForm;
import com.fastcampus.survey.questionary.domain.repository.SurveyFormRepository;
import com.fastcampus.survey.questionary.adapter.out.repository.SurveyFormJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class SurveyFormRepositoryImpl implements SurveyFormRepository {
    private final SurveyFormJpaRepository jpaRepository;

    public SurveyFormRepositoryImpl(SurveyFormJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public SurveyForm save(SurveyForm form) {
        // TODO: SurveyForm <-> SurveyFormJpaEntity 매핑 필요
        // 임시로 null 반환
        return null;
    }

    @Override
    public Optional<SurveyForm> findById(Long id) {
        // TODO: SurveyFormJpaEntity <-> SurveyForm 매핑 필요
        // 임시로 Optional.empty() 반환
        return Optional.empty();
    }
} 