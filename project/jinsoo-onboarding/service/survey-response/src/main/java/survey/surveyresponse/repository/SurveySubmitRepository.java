package survey.surveyresponse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import survey.surveyresponse.entity.SurveySubmit;

@Repository
public interface SurveySubmitRepository extends JpaRepository<SurveySubmit, Long> {
}
