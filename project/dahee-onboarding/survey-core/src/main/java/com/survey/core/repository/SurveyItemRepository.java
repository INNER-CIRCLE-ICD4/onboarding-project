package com.survey.core.repository;

import com.survey.core.entity.SurveyItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SurveyItemRepository extends JpaRepository<SurveyItem, Long> {
    List<SurveyItem> findBySurveyId(Long surveyId);

    List<SurveyItem> findBySurveyIdAndIsDeletedFalse(Long id);

}
