package fastcampus.inguk_onboarding.form.post.jpa;

import fastcampus.inguk_onboarding.form.post.repository.entity.post.SurveyVersionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface JpaSurveyVersionRepository extends JpaRepository<SurveyVersionEntity, Long> {
    
    @Query("SELECT COUNT(v) FROM SurveyVersionEntity v WHERE v.survey.id = :surveyId")
    Long countBySurveyId(@Param("surveyId") Long surveyId);

    @Query("SELECT v FROM SurveyVersionEntity v WHERE v.survey.id = :surveyid")
    Long findBySurveyId(@Param("surveyId") Long surveyId);

    @Query("SELECT sv FROM SurveyVersionEntity sv " +
           "JOIN FETCH sv.survey " +
           "LEFT JOIN FETCH sv.items " +
           "WHERE sv.id = :id")
    Optional<SurveyVersionEntity> findByIdWithSurveyAndItems(@Param("id") Long id);
} 