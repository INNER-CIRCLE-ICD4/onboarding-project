package com.example.byeongjin_onboarding.repository;

import com.example.byeongjin_onboarding.entity.SurveyAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SurveyAnswerRepository extends JpaRepository<SurveyAnswer, Long> {
    @Query("SELECT sa FROM SurveyAnswer sa JOIN FETCH sa.answerItems ai JOIN FETCH ai.formItem fi WHERE sa.survey.id = :surveyId")
    List<SurveyAnswer> findAllBySurveyIdWithAnswerItems(@Param("surveyId") Long surveyId);
}