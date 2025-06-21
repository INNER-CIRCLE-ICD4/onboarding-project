package com.survey.choheeonboarding.api.repository;

import com.survey.choheeonboarding.api.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyRepo extends JpaRepository<Survey, String> {

}
