package com.INNER_CIRCLE_ICD4.innerCircle.survey.request.repository;

import com.INNER_CIRCLE_ICD4.innerCircle.survey.request.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}