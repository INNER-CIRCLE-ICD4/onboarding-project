package com.multi.sungwoongonboarding.submission.infrastructure.submission;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubmissionJpaRepository extends JpaRepository<SubmissionJpaEntity, Long> {
    
    List<SubmissionJpaEntity> findByFormId(Long formId);
    
    List<SubmissionJpaEntity> findByUserId(String userId);
}