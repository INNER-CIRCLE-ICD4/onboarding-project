package survey.survey.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import survey.survey.entity.surveyquestion.SurveyQuestion;

import java.util.List;

@Repository
public interface SurveyQuestionRepository extends JpaRepository<SurveyQuestion, Long> {
    List<SurveyQuestion> findSurveyQuestionBySurveyFormId(Long surveyFormId);

    List<SurveyQuestion> findSurveyQuestionBySurveyFormIdAndDeletedFalse(Long surveyFormId);
}
