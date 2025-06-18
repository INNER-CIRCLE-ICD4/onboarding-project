package com.group.surveyapp.repository;

import com.group.surveyapp.domain.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyRepository extends JpaRepository<Survey, Long> {
}
