package com.multi.sungwoongonboarding.questions.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface QuestionJpaRepository extends JpaRepository<QuestionJpaEntity, Long> {

    @Query("""
            SELECT q
            FROM QuestionJpaEntity q
            left join fetch q.options
            where q.id = :id
            """)
    Optional<QuestionJpaEntity> findById(Long id);
}
