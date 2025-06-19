package com.multi.sungwoongonboarding.forms.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FormJpaRepository extends JpaRepository<FormsJpaEntity, Long> {

}
