package fastcampus.inguk_onboarding.form.response.jpa;

import fastcampus.inguk_onboarding.form.response.repository.entity.response.SurveyResponseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaSurveyResponseRepository extends JpaRepository<SurveyResponseEntity, Long> {

    @Query("SELECT r FROM SurveyResponseEntity r WHERE r.surveyId = :surveyId")
    List<SurveyResponseEntity> findBySurveyId(@Param("surveyId") Long surveyId);
    
    @Query("SELECT r FROM SurveyResponseEntity r WHERE r.surveyVersionId = :surveyVersionId")
    List<SurveyResponseEntity> findBySurveyVersionId(@Param("surveyVersionId") Long surveyVersionId);


}
