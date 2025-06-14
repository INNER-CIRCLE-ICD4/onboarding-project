package fc.icd.jaehyeononboarding.survey.repository;

import fc.icd.jaehyeononboarding.survey.model.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
