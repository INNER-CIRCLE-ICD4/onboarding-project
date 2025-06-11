package com.INNER_CIRCLE_ICD4.innerCircle.survey.request.repository;

import com.INNER_CIRCLE_ICD4.innerCircle.survey.request.domain.QuestionOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<QuestionOption, Long> {
}