package com.INNER_CIRCLE_ICD4.innerCircle.repository;
import com.INNER_CIRCLE_ICD4.innerCircle.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface QuestionRepository extends JpaRepository<Question, UUID> {
}