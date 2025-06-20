package fastcampus_inner_circle.junbeom_onboarding.survey.questionary.adapter.out.impl;

import fastcampus_inner_circle.junbeom_onboarding.survey.questionary.adapter.in.mapper.SurveyFormJpaEntityMapper;
import fastcampus_inner_circle.junbeom_onboarding.survey.questionary.domain.model.SurveyForm;
import fastcampus_inner_circle.junbeom_onboarding.survey.questionary.domain.repository.SurveyFormRepository;
import fastcampus_inner_circle.junbeom_onboarding.survey.questionary.adapter.out.repository.SurveyFormJpaRepository;
import fastcampus_inner_circle.junbeom_onboarding.survey.questionary.adapter.out.entity.SurveyFormJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SurveyFormRepositoryImpl implements SurveyFormRepository {

    private final SurveyFormJpaRepository jpaRepository;
    private final SurveyFormJpaEntityMapper surveyFormJpaEntityMapper;

    @Override
    public SurveyForm save(SurveyForm form) {
        SurveyFormJpaEntity entity = surveyFormJpaEntityMapper.toJpaEntity(form);
        SurveyFormJpaEntity saved = jpaRepository.save(entity);
        return surveyFormJpaEntityMapper.toDomain(saved);
    }

    @Override
    public Optional<SurveyForm> findById(Long id) {
        return jpaRepository.findById(id)
                .map(surveyFormJpaEntityMapper::toDomain);
    }
} 