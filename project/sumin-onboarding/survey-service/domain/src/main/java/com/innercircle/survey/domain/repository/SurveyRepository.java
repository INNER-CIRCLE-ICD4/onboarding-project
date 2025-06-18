package com.innercircle.survey.domain.repository;

import com.innercircle.survey.domain.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SurveyRepository extends JpaRepository<Survey, UUID> {

}
