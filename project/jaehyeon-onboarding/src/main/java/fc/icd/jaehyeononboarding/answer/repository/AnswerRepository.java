package fc.icd.jaehyeononboarding.answer.repository;

import fc.icd.jaehyeononboarding.answer.model.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByQuestionGroupIdIn(List<Long> questionGroupIds);
}
