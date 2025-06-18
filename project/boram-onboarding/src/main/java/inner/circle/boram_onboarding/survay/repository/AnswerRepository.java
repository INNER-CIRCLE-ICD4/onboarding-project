package inner.circle.boram_onboarding.survay.repository;

import inner.circle.boram_onboarding.survay.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByResponseId(Long responseId);
}

