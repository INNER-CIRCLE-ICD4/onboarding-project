package kr.co.fastcampus.onboarding.hyeongminonboarding.domain.repository;

import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long>, QuestionRepositoryCustom {
}
