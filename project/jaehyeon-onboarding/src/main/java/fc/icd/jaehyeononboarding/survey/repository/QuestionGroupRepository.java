package fc.icd.jaehyeononboarding.survey.repository;

import fc.icd.jaehyeononboarding.survey.model.entity.QuestionGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionGroupRepository extends JpaRepository<QuestionGroup, Long> {
}
