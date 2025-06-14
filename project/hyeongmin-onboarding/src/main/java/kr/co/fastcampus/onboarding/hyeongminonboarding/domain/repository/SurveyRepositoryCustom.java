package kr.co.fastcampus.onboarding.hyeongminonboarding.domain.repository;

import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.Survey;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;


public interface SurveyRepositoryCustom {
    Optional<Survey> findByIdQueryDsl(Long id);
    long updateNameById(Long id, String name);
}
