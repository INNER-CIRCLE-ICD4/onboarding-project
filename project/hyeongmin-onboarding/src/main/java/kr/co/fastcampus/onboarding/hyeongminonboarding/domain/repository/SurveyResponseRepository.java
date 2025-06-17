package kr.co.fastcampus.onboarding.hyeongminonboarding.domain.repository;

import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.SurveyResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SurveyResponseRepository extends JpaRepository<SurveyResponse, Long>,SurveyResponseRepositoryCustom {
    List<SurveyResponse> findAllBySurveyIdOrderBySubmittedAtAsc(Long surveyId);
}
