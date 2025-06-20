package fastcampus_inner_circle.junbeom_onboarding.survey.questionary.adapter.out.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fastcampus_inner_circle.junbeom_onboarding.survey.questionary.adapter.out.entity.SurveyFormJpaEntity;

public interface SurveyFormJpaRepository extends JpaRepository<SurveyFormJpaEntity, Long> {
} 