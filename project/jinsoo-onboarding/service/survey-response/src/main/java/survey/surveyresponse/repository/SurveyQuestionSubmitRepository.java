package survey.surveyresponse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import survey.surveyresponse.entity.SurveyQuestionSubmit;

@Repository
public interface SurveyQuestionSubmitRepository extends JpaRepository<SurveyQuestionSubmit, Long> {
}
