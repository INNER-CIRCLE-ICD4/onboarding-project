package com.icd.onboarding.survey.repository;

import com.icd.onboarding.survey.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findBySurveyId(Long surveyId);
}
