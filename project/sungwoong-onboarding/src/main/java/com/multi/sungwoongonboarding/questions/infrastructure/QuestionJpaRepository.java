package com.multi.sungwoongonboarding.questions.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionJpaRepository extends JpaRepository<QuestionJpaEntity, Long> {
}
