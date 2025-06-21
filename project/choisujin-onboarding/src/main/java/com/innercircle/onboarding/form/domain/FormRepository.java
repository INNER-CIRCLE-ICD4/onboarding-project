package com.innercircle.onboarding.form.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FormRepository extends JpaRepository<Form, Long> {
    Form findByUuid(UUID uuid);
}
