package fastcampus.inguk_onboarding.form.post.jpa;

import fastcampus.inguk_onboarding.form.post.repository.entity.post.SurveyVersionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaSurveyVersionRepository extends JpaRepository<SurveyVersionEntity, Long> {
    
    @Query("SELECT COUNT(v) FROM SurveyVersionEntity v WHERE v.survey.id = :surveyId")
    Long countBySurveyId(@Param("surveyId") Long surveyId);
} 