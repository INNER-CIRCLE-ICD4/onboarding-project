package com.survey.choheeonboarding.api.repository;

import com.survey.choheeonboarding.api.entity.SurveyQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SurveyQuestionRepo extends JpaRepository<SurveyQuestion, String> {

    List<SurveyQuestion> findBySurveyId(String surveyId);

}
