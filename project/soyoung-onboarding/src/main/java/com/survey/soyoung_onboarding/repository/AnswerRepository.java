package com.survey.soyoung_onboarding.repository;

import com.survey.soyoung_onboarding.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    List<Answer> findBySurveyId(UUID surveyId);

}
