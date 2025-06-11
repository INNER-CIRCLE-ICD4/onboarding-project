package com.INNER_CIRCLE_ICD4.innerCircle.survey.request.repository;

import com.INNER_CIRCLE_ICD4.innerCircle.survey.request.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}