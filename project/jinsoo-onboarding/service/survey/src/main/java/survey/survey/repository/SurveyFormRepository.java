package survey.survey.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import survey.survey.entity.surveyform.SurveyForm;

@Repository
public interface SurveyFormRepository extends JpaRepository<SurveyForm, Long> {
}
