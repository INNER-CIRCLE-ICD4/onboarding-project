package com.survey.core.repository;

import com.survey.core.entity.SurveyItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyItemRepository extends JpaRepository<SurveyItem, Long> {
}
