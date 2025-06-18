package com.survey.core.repository;

import com.survey.core.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SurveyRepository extends JpaRepository<Survey, Long> {
    Optional<Object> findTopByIdOrderByVersionDesc(Long surveyId);

    Optional<Object> findByIdAndVersion(Long surveyId, Integer version);
}
