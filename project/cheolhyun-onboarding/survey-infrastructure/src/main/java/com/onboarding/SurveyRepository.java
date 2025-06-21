package com.onboarding;

import com.onboarding.entity.SurveyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SurveyRepository extends JpaRepository<SurveyEntity, UUID> {
}
