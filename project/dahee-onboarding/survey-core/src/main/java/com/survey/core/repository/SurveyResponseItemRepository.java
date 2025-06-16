package com.survey.core.repository;

import com.survey.core.entity.SurveyResponseItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyResponseItemRepository extends JpaRepository<SurveyResponseItem, Long> {
}
