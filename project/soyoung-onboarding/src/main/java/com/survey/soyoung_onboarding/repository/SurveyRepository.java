package com.survey.soyoung_onboarding.repository;

import com.survey.soyoung_onboarding.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SurveyRepository extends JpaRepository<Survey, UUID> {

}
