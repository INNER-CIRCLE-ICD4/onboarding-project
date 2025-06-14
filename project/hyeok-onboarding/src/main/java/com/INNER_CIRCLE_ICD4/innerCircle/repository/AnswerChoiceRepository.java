package com.INNER_CIRCLE_ICD4.innerCircle.repository;

import com.INNER_CIRCLE_ICD4.innerCircle.domain.AnswerChoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AnswerChoiceRepository extends JpaRepository<AnswerChoice, UUID> {
}