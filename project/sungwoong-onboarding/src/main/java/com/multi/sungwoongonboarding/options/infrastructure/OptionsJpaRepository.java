package com.multi.sungwoongonboarding.options.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OptionsJpaRepository extends JpaRepository<OptionsJpaEntity, Long> {


    @Query("""
                select  o
                from OptionsJpaEntity o
                left join fetch o.questionJpaEntity q
                left join fetch q.formsJpaEntity f
                where f.id = :formId
            """)
    List<OptionsJpaEntity> findByFormId(Long formId);
}
