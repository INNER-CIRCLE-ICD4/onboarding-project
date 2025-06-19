package survey.surveyread.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import survey.surveyread.entity.SurveyResponseQueryModel;

import java.util.List;

public interface SurveyResponseQueryModelRepository extends JpaRepository<SurveyResponseQueryModel, Long> {
    List<SurveyResponseQueryModel> findAllBySurveyId(Long surveyId);
}