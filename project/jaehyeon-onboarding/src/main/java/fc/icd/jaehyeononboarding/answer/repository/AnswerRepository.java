package fc.icd.jaehyeononboarding.answer.repository;

import fc.icd.jaehyeononboarding.answer.model.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
