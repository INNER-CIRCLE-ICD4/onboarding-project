package survey.survey.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import survey.survey.entity.surveyform.SurveyForm;

import java.util.Optional;

@Repository
public interface SurveyFormRepository extends JpaRepository<SurveyForm, Long> {
    @Query("SELECT s.id FROM SurveyForm s WHERE s.surveyId = :surveyId ORDER BY s.version DESC LIMIT 1")
    Optional<Long> findCurrentSurveyFormId(Long surveyId);


}
