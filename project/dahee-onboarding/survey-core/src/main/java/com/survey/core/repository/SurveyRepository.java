package com.survey.core.repository;

import com.survey.core.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyRepository extends JpaRepository<Survey, Long> {
}
