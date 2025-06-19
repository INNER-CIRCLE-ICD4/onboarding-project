package fc.icd.baulonboarding.survey.repository.entity;

import fc.icd.baulonboarding.survey.model.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyRepository extends JpaRepository<Survey, Long> {
}
