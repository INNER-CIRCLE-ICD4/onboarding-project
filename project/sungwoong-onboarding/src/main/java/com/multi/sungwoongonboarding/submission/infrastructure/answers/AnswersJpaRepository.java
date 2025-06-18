package com.multi.sungwoongonboarding.submission.infrastructure.answers;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswersJpaRepository extends JpaRepository<AnswerJpaEntity, Long> {
    
    List<AnswerJpaEntity> findByResponseJpaEntityId(Long responseId);
    
    List<AnswerJpaEntity> findByQuestionId(Long questionId);
}