package inner.circle.boram_onboarding.survay.repository;

import inner.circle.boram_onboarding.survay.entity.Response;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResponseRepository extends JpaRepository<Response, Long> {
    List<Response> findBySurveyId(Long surveyId);
}