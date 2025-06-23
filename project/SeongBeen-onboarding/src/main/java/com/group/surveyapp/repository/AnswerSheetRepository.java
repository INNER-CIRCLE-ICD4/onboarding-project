package com.group.surveyapp.repository;

import com.group.surveyapp.domain.entity.AnswerSheet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerSheetRepository extends JpaRepository<AnswerSheet, Long> {
    List<AnswerSheet> findAllBySurveyId(Long surveyId);
}
