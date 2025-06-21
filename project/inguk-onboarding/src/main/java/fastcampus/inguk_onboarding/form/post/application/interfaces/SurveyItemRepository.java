package fastcampus.inguk_onboarding.form.post.application.interfaces;

import fastcampus.inguk_onboarding.form.post.repository.entity.post.SurveyItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyItemRepository  extends JpaRepository<SurveyItemEntity, Long> {

}
