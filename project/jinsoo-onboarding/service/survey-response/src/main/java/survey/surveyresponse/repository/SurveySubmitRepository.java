package survey.surveyresponse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import survey.surveyresponse.entity.SurveySubmit;

import java.util.Optional;

@Repository
public interface SurveySubmitRepository extends JpaRepository<SurveySubmit, Long> {
    Optional<SurveySubmit> findBySurveyId(Long surveyId);
}

