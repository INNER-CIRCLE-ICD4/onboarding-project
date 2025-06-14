package com.INNER_CIRCLE_ICD4.innerCircle.repository;

import com.INNER_CIRCLE_ICD4.innerCircle.domain.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SurveyRepository extends JpaRepository<Survey, UUID> {

}