package com.INNER_CIRCLE_ICD4.innerCircle.survey.request.repository;

import com.INNER_CIRCLE_ICD4.innerCircle.survey.request.domain.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyRepository extends JpaRepository<Survey, Long> {
}