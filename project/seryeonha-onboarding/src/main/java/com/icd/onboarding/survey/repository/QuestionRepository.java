package com.icd.onboarding.survey.repository;

import com.icd.onboarding.survey.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findAllBySurveyIdAndSurveyVersion(Long surveyId, Integer surveyVersion);
}
