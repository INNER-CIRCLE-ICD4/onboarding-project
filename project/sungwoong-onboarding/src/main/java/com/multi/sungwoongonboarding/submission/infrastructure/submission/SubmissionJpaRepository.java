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
                and (:questionText is null or 
                            exists (
                                select sub_a.id 
                                from AnswerJpaEntity sub_a 
                                left join QuestionJpaEntity q 
                                on sub_a.questionId = q.id 
                                where sub_a.submissionJpaEntity.id = s.id 
                                and q.questionText like concat('%', :questionText, '%'))
                ) 
                and (:answerText is null or 
                            exists (
                                select sub_a.id
                                from AnswerJpaEntity sub_a 
                                left join OptionsJpaEntity o on sub_a.optionId = o.id
                                where sub_a.submissionJpaEntity.id = s.id
                                and (sub_a.answerText like concat('%', :answerText, '%') or o.optionText like concat('%', :answerText, '%')))
                ) 
            """)
    List<SubmissionJpaEntity> findByFormId(@Param("formId") Long formId, @Param("questionText") String questionText, @Param("answerText") String answerText);
}