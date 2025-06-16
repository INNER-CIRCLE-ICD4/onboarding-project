package com.multi.sungwoongonboarding.questions.infrastructure;

import com.multi.sungwoongonboarding.forms.infrastructure.FormsJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface QuestionJpaRepository extends JpaRepository<QuestionJpaEntity, Long> {

    @Query("""
            SELECT q
            FROM QuestionJpaEntity q
            left join fetch q.options
            where q.id = :id
            """)
    Optional<QuestionJpaEntity> findById(Long id);

    List<QuestionJpaEntity> findByFormsJpaEntity(FormsJpaEntity formsJpaEntity);
}
