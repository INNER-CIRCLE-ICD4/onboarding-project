package fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.out.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.out.entity.AnswerJpaEntity;

public interface AnswerJpaRepository extends JpaRepository<AnswerJpaEntity, Long> {

    List<AnswerJpaEntity> findByFormId(Long formId);

} 