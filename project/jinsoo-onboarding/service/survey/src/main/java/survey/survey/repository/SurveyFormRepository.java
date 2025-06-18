package survey.survey.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import survey.survey.entity.surveyform.SurveyForm;

import java.util.Optional;

@Repository
public interface SurveyFormRepository extends JpaRepository<SurveyForm, Long> {
    Optional<SurveyForm> findTopBySurveyIdOrderByVersionDesc(Long surveyId);

}
