package com.multi.sungwoongonboarding.forms.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FormHistoryJpaRepository extends JpaRepository<FormHistoryJpaEntity, Long> {
    List<FormHistoryJpaEntity> findByForm_Id(Long formId);
}
