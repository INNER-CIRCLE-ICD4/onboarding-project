package com.multi.sungwoongonboarding.responses.infrastructure.responses;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResponsesJpaRepository extends JpaRepository<ResponseJpaEntity, Long> {
    
    List<ResponseJpaEntity> findByFormId(Long formId);
    
    List<ResponseJpaEntity> findByUserId(String userId);
}