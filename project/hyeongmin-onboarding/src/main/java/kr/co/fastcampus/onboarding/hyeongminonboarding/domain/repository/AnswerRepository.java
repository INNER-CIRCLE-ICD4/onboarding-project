package kr.co.fastcampus.onboarding.hyeongminonboarding.domain.repository;

import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> ,AnswerRepositoryCustom {
}
