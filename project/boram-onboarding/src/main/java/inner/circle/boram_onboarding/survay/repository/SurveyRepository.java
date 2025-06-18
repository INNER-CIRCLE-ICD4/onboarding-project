package inner.circle.boram_onboarding.survay.repository;

import inner.circle.boram_onboarding.survay.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyRepository extends JpaRepository<Survey,Long> {



}
