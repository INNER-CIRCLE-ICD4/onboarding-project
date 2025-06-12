package fc.icd.baulonboarding.survey.repository.entity;

import fc.icd.baulonboarding.survey.model.entity.SurveyItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyItemRepository extends JpaRepository<SurveyItem, Long> {
}
