package inner.circle.boram_onboarding.survay.repository;

import inner.circle.boram_onboarding.survay.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findBySurveyId(Long surveyId);
}