package fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.out.repository;

import fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.out.entity.AnswerJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AnswerJpaRepository extends JpaRepository<AnswerJpaEntity, Long> {

} 