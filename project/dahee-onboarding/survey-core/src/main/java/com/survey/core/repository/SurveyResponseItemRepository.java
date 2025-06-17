package com.survey.core.repository;

import com.survey.core.entity.SurveyResponseItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SurveyResponseItemRepository extends JpaRepository<SurveyResponseItem, Long> {
    List<SurveyResponseItem> findByResponseIdIn(List<Long> responseIds);
}
