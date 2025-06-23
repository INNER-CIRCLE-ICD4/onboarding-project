package com.innercircle.survey.domain.repository;

import com.innercircle.survey.domain.entity.Survey;
import com.innercircle.survey.domain.entity.SurveyResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SurveyResponseRepository extends JpaRepository<SurveyResponse, UUID> {
}
