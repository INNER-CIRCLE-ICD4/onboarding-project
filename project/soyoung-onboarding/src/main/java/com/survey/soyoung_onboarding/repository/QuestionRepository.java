package com.survey.soyoung_onboarding.repository;

import com.survey.soyoung_onboarding.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface QuestionRepository extends JpaRepository<Question,Long> {

    List<Question> findBySurveyId(UUID surveyId);

}
