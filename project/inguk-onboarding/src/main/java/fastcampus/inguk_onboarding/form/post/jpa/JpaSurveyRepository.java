package fastcampus.inguk_onboarding.form.post.jpa;

import fastcampus.inguk_onboarding.form.post.repository.entity.post.SurveyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface JpaSurveyRepository extends JpaRepository<SurveyEntity, Long> {


    @Modifying
    @Query(value="UPDATE SurveyEntity p " +
            "SET p.description = :#{#surveyEntity.getDescription()}," +
            "p.title = :#{#surveyEntity.getTitle()}," +
            "p.updatedAt = now()" +
            "WHERE p.id = :#{#surveyEntity.getId()} ")
    void updateSurveyEntity(SurveyEntity surveyEntity);


}
