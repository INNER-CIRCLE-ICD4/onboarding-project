package com.multi.sungwoongonboarding.submission.infrastructure.submission;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubmissionJpaRepository extends JpaRepository<SubmissionJpaEntity, Long> {

    List<SubmissionJpaEntity> findByFormId(Long formId);

    List<SubmissionJpaEntity> findByUserId(String userId);

    @Query("""
                select s
                from SubmissionJpaEntity s
                left join FormsJpaEntity f on s.formId = f.id
                where s.formId = :formId
                and (:questionText is null 
                    or f.id in (select q.formsJpaEntity.id from QuestionJpaEntity q where q.formsJpaEntity.id = f.id and q.questionText like  %:questionText%))
                and (:answerText is null 
                    or s.id in (select s.id from AnswerJpaEntity a left join a.submissionJpaEntity s left join OptionsJpaEntity o on o.id = a.optionId where (a.answerText like %:answerText% or o.optionText like %:answerText%)))
            """)
    List<SubmissionJpaEntity> findByFormId(@Param("formId") Long formId, @Param("questionText") String questionText, @Param("answerText") String answerText);
}