package fc.icd.jaehyeononboarding.survey.repository;

import fc.icd.jaehyeononboarding.survey.model.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SurveyRepository extends JpaRepository<Survey, Long> {

    @Query("SELECT s FROM Survey s " +
            "INNER JOIN QuestionGroup qg ON " +
            "s.id = qg.survey.id " +
            "AND s.latestVersion = qg.version " +
            "WHERE s.id = :surveyId")
    Survey findSurveyWithLatestVersionQuestionGroup(@Param("surveyId") Long surveyId);
}
